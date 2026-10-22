# HANDOFF — English Center Management System

> Context để một session AI mới hiểu và tiếp tục công việc. **Cập nhật: 2026-10-22.**
> Repo: https://github.com/ngsang149dev/english-center-project (public)
> File này là nguồn sự thật về *cách làm việc* và *trạng thái code* — đọc hết trước khi làm bất cứ gì.

---

## 0) Cách làm việc (bắt buộc tuân thủ)

Tôi là **Sang** — sinh viên EIU, **tự viết code backend**. AI đóng vai senior hướng dẫn + review.

- **Tôi code, AI không viết thay cả file.** AI giải thích *why*, chỉ đưa snippet ngắn cho phần mới/nhạy cảm (security, config, JPA mapping lạ).
- **Hỏi trước khi chốt design option** — đưa 2–3 lựa chọn kèm tradeoff, đừng tự quyết.
- **Review diff theo thứ tự ưu tiên**, gắn nhãn:
  - **P0** — security / secret / exception handling → *must-fix*
  - **P1** — transaction boundary, query/N+1, DTO leak → *should-fix*
  - **P2** — pagination, audit field, test → *optional*
- **Không viết lại cả project.** Chỉ sửa đúng chỗ; đừng paste cả file khi chỉ đổi vài dòng.
- Trả lời **ngắn, tiếng Việt**; code/comment **tiếng Anh**. Lệnh macOS zsh chính xác + kết quả mong đợi.
- **Không bao giờ** yêu cầu tôi dán password/token/nội dung `.env`. Không in secret ra output.
- **Không claim "đã test"** nếu chưa chạy thật. Nói "đã merge" thì phải verify bằng `git log origin/main`.
- Ưu tiên hoàn thành trọn một block rồi mới báo cáo.

## 1) Sản phẩm

Backend-only cho trung tâm Anh ngữ: user/teacher/student, lớp + lịch + buổi học, ghi danh, điểm danh, học phí (invoice + payment), lương giáo viên (rate + payroll), chi phí, báo cáo tháng. Frontend làm sau.

Mục tiêu: **đồ án tốt nghiệp + portfolio**, MVP, horizon 6–12 tháng. Ưu tiên đúng nghiệp vụ > số lượng feature.

## 2) Stack & môi trường

| | |
|---|---|
| Java | 25 (Temurin 25.0.1) |
| Spring Boot | **4.1.1** (Framework 7.0.9, Hibernate 7.4, Security 7.1, Jackson 3) |
| DB | MySQL local Homebrew, database `english_center` |
| Build | Maven wrapper `./mvnw` |
| Auth | JWT HS256, stateless |
| Migration | **chưa có** — `ddl-auto=update` |

Package root `com.abuenglishcenter.managementsystem` — **package-by-feature** (mỗi feature 1 folder: entity + repo + service + controller + dto).

## 3) Trạng thái repo — ĐỌC TRƯỚC KHI LÀM

```
origin/main = 6491407   Merge PR #12 (C1 attendance)
local  main = a763444   behind 3 commits
```

**Việc đầu tiên của session mới:**

```zsh
cd ~/Code/Projects/english-center-project
git fetch --prune
git switch main && git pull --ff-only
git log --oneline -5      # dòng đầu phải là 6491407
```

- Đã merge: `feature/b7-auth-login` (#10), `feature/b1b-create-user` (#11), `feature/c1-attendance` (#12).
- Remote còn branch cũ (b2, b4, b5, b6) — **không xoá**.
- `.dsh/` đang untracked (skill của agent) — **không commit**.
- Branch mới: `git switch -c feature/cX-ten-ngan` (dùng đúng `feature/`, từng gõ nhầm `freature/` và phải rename cả remote).

## 4) Đã xong (verified)

### B7 — Auth (PR #10)
- `POST /auth/login` public → `{accessToken, tokenType:"Bearer", expiresIn:28800, user}`; `GET /auth/me` authenticated.
- Claims: `sub` = user id, `role` = Role name.
- `SecurityConfig`: `/auth/login` permitAll, `/auth/me` authenticated, **`anyRequest().hasRole("ADMIN")`**.
- Lỗi security trả `application/problem+json`: `ProblemDetailSecurityHandlers` implements **cả** `AuthenticationEntryPoint` và `AccessDeniedHandler`, gắn **cả hai chỗ**:
  - `exceptionHandling(...)` → cho 403/entry point thường
  - `oauth2ResourceServer(...).authenticationEntryPoint(...)` → cho token sai định dạng (nếu thiếu, trả 401 body rỗng)
- `JwtAuthenticationConverter` map claim `role` + prefix `ROLE_`.

### B1b — POST /users (PR #11)
- Chỉ tạo được **ADMIN**. TEACHER/STUDENT → **409** `"Only ADMIN accounts can be created here. Use POST /teachers or POST /students"`.
- DTO có `@NotNull role` → thiếu role = **400** + `errors.role`; gửi role khác ADMIN = **409**.
- Trùng username → **409**.

### C1 — Attendance (PR #12, commit b7fe29d)
- Bảng `attendances` có unique `(session_id, enrollment_id)`.
- `POST /attendances` (batch): chặn trùng **trong request** (409), trùng **DB** (409), enrollment **không thuộc lớp** của session (409), 404 nếu session/enrollment không tồn tại. Có `@Transactional` → 1 item fail thì rollback cả batch.
- `GET /attendances?sessionId=&enrollmentId=` lọc 4 nhánh; **không filter → `findAll()`** (tạm, pagination để C8).
- `PUT /attendances/{id}` chỉ đổi `status`.

Test C1: **14/14 PASS** (A1–A6 tạo/validate, F1–F3 filter, U1–U4 update, Z1 401).

## 5) Quy ước code đang dùng

- Controller → Service → Repository. **Constructor injection**, field `private final`.
- **Không trả entity ra ngoài** — luôn DTO (`XxxResponseDto`), map bằng `private toDto` trong service.
- Method ghi dữ liệu: **`@Transactional`**.
- So sánh `Long` bằng `.equals()`, **không** `==`.
- Error mapping (`GlobalExceptionHandler`):
  - `BusinessRuleException` → **409**
  - `NotFoundException` → **404**
  - validation `@Valid` → **400** + `errors: {field: message}`
  - `DataIntegrityViolationException` → 409 generic
  - tất cả đều là `ProblemDetail`
- `@ManyToOne` mặc định **EAGER** — cẩn thận N+1 khi list lớn.

Enums hiện có:

```
Role             ADMIN, TEACHER, STUDENT
StudentStatus    ACTIVE, INACTIVE, ALUMNI
EnrollmentStatus ACTIVE, WITHDRAWN, COMPLETED
AttendanceStatus PRESENT, ABSENT, LATE
InvoiceStatus    UNPAID, PARTIALLY_PAID, PAID
PaymentMethod    BANKING, CASH
PayrollStatus    DRAFT, CONFIRMED, PAID   (+ canMoveTo)
ExpenseCategory  RENT, UTILITIES, STAFF_SALARY, MARKETING, SUPPLIES, OTHER
```

## 6) Endpoint hiện có

Tất cả **trừ `/auth/**`** đều yêu cầu **ROLE_ADMIN**.

| Method | Path |
|---|---|
| POST | `/auth/login` (public) |
| GET | `/auth/me` (authenticated) |
| GET, POST | `/users` |
| GET, POST | `/teachers` |
| GET, POST | `/students` |
| GET, POST | `/classes` |
| GET | `/classes/{id}/schedules` |
| PUT | `/classes/{id}/teacher` |
| GET, POST | `/class-schedules` |
| GET, POST | `/class-sessions` |
| PUT | `/class-sessions/{id}` |
| PUT | `/class-sessions/{id}/teacher` |
| GET, POST | `/class-fee-history` |
| GET, POST | `/enrollments` |
| GET, POST | `/attendances` (GET nhận `sessionId`, `enrollmentId`) |
| PUT | `/attendances/{id}` |
| GET, POST | `/invoices` |
| GET | `/invoices/balance` |
| POST | `/invoices/generate` |
| PUT | `/invoices/{id}/amount` |
| GET, POST | `/payments` |
| PUT | `/payments/{id}/cancel` |
| GET, POST | `/teacher-rates` |
| GET, POST | `/teacher-payrolls` |
| PUT | `/teacher-payrolls/{id}/bonus` |
| PUT | `/teacher-payrolls/{id}/status` |
| POST | `/teacher-payrolls/{id}/recalculate` |
| DELETE | `/teacher-payrolls/{id}` |
| GET, POST | `/expenses` |
| GET | `/reports/monthly` |

⚠️ TEACHER/STUDENT hiện **không gọi được** endpoint nào ngoài `/auth/me` — đó là **B7b**, đang chờ chốt Q3.

## 7) Việc tiếp theo

1. **Pull main** (mục 3).
2. **C2 — Enrollment rules** (đề xuất làm tiếp):
   - chặn ghi danh trùng (cùng student + class khi còn ACTIVE) → 409
   - chặn học sinh `INACTIVE` (chờ chốt Q6)
   - thêm `PUT /enrollments/{id}/status` với rule chuyển trạng thái (ACTIVE ↔ WITHDRAWN/COMPLETED)
3. **C3 — Profile endpoints**: hiện chỉ có GET list + POST cho `/students`, `/teachers`; cần `GET /{id}`, `PUT /{id}`.
4. Sau đó: **B7b** (cần Q3), **C8** (pagination), rồi hardening billing/payroll.

Backlog gốc còn **C5, C6, C7** — file gốc đã mất, session mới cần hỏi lại tôi trước khi làm.

## 8) Câu hỏi design còn mở (hỏi tôi trước khi code phần liên quan)

| # | Câu hỏi | Đề xuất của tôi |
|---|---|---|
| Q3 | B7b: TEACHER xem payroll của mình? STUDENT xem invoice của mình? | Có, read-only qua `/me/...` |
| Q5 | Payment vượt số tiền còn lại của invoice | 409, không cho overpay |
| Q6 | Student `INACTIVE` | Chặn login + chặn ghi danh mới; **không** tự rút ghi danh cũ |
| Q7 | Session tạo tay có `teacherTaught=false`? | Có, mặc định `false` |
| Q8 | Pagination (C8) | Làm sau, chốt response `Page<T>` trước |
| Q9 | Docker hoá | Để cuối, trước khi nộp |
| Q11 | Rounding tiền | `HALF_UP`, scale 2 |
| Q12 | Scale cột DECIMAL | `DECIMAL(12,2)` cho tiền |

## 9) Gotchas

- **MySQL phải chạy trước khi start app.** Hiện tại (2026-10-22) MySQL **đang tắt** → `brew services start mysql`.
- App ở port **8080**; chạy lại thì phải tắt trước, không thì `Port 8080 already in use`.
- `.env` **không commit** (`backend/.env` nằm trong `.gitignore`); `.env.example` là mẫu. Key cần: `DB_USERNAME`, `DB_PASSWORD`, `APP_ADMIN_USERNAME`, `APP_ADMIN_PASSWORD`, `JWT_SECRET`, `SPRING_PROFILES_ACTIVE`.
- `JWT_SECRET` phải **≥ 32 bytes**, không thì app throw `IllegalStateException` lúc start.
- Token sống **480 phút**. Gặp 401 bất ngờ → login lại lấy token mới, đừng đoán.
- `ADMIN_TOKEN` trong `.env` chỉ là tiện dụng local và **hay hết hạn** — luôn login mới mỗi lần test.
- `AdminSeeder` chỉ seed ADMIN khi DB **chưa có** admin nào.
- `ddl-auto=update` không xoá dữ liệu trùng cũ và không phải cách làm production — sẽ cần Flyway.
- Profile `dev` bật `show-sql=true` → log rất dài, đừng hoảng.
- CORS chỉ cho `http://localhost:5173` và `http://localhost:3000`.
- Test xong phải **xoá dữ liệu test**. DB local từng có seed admin + teacher1/2 + student1/2 — chạy SQL ở mục 10 để xem chính xác hiện tại.

## 10) Lệnh vận hành (macOS zsh)

```zsh
# 1) MySQL
brew services start mysql
brew services list | grep mysql          # phải là started

# 2) Compile
cd ~/Code/Projects/english-center-project/backend
./mvnw -q -DskipTests compile

# 3) Chạy app (nạp .env, không echo secret)
( set -a; . ./.env; set +a; ./mvnw -q spring-boot:run ) > /tmp/app.log 2>&1 &
sleep 25 && tail -5 /tmp/app.log          # thấy "Started ManagementsystemApplication"

# 4) Lấy token + smoke test
set -a; . backend/.env; set +a
TOKEN=$(curl -s -X POST localhost:8080/auth/login -H 'Content-Type: application/json' \
  -d "{\"username\":\"$APP_ADMIN_USERNAME\",\"password\":\"$APP_ADMIN_PASSWORD\"}" | jq -r .accessToken)
curl -s -o /dev/null -w '%{http_code}\n' localhost:8080/auth/me -H "Authorization: Bearer $TOKEN"   # 200

# 5) Tắt app
pkill -f spring-boot:run ; sleep 2
lsof -nP -iTCP:8080 -sTCP:LISTEN          # phải trống

# 6) Query DB
set -a; . backend/.env; set +a
mysql -u "$DB_USERNAME" -p"$DB_PASSWORD" english_center \
  -e "SELECT id, username, role FROM users ORDER BY id;"
```

## 11) Quy trình làm 1 feature (đã áp dụng cho B1b, C1)

1. **Preflight**: `git switch main && git pull --ff-only`; working tree sạch; tạo branch `feature/cX-...`.
2. Chốt design (hỏi tôi) → tôi code → AI review **P0 → P1 → P2**.
3. **Viết test protocol trước khi chạy**, 4 nhóm:
   - **A** — happy path + validation (200/400/404/409)
   - **F** — filter/query
   - **U** — update
   - **Z** — auth (401/403)
   Mỗi case: *input → expected status → body mong đợi*. Chạy bằng `curl` thật, ghi lại kết quả thật.
4. **Dọn dữ liệu test** (xoá row vừa tạo) và kiểm tra lại DB.
5. Commit message rõ nghĩa (imperative), push, tạo PR base `main`, merge, `git switch main && git pull`.

## 12) Prompt gợi ý cho session mới

> Đọc `HANDOFF.md` trong repo. Xác nhận lại state: branch/commit hiện tại, việc đã xong, việc kế tiếp. **Chưa code gì cho tới khi tôi chọn việc.** Giữ đúng cách làm việc ở mục 0 — tôi viết code, bạn hướng dẫn và review theo P0/P1/P2.
