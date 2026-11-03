# HANDOFF — English Center Management System

> Context để một session AI mới hiểu và tiếp tục công việc. **Cập nhật: 2026-11-03.**
> Repo: https://github.com/ngsang149dev/english-center-project (public)
> File này là nguồn sự thật về *cách làm việc* và *trạng thái code* — đọc hết trước khi làm bất cứ gì.

---

## 0) Cách làm việc (bắt buộc tuân thủ)

Tôi là **Sang** — sinh viên EIU, **tự viết code backend**. AI đóng vai senior hướng dẫn + review.

- **Tôi code, AI không viết thay cả file.** AI giải thích *why*, chỉ đưa snippet ngắn cho phần mới/nhạy cảm (security, config, JPA mapping lạ).
- **Ngoại lệ**: khi tôi nói rõ "làm giúp tôi" / "bạn thấy cách nào tối ưu thì làm" thì AI được code, commit, push, mở PR — vẫn phải báo rõ đã làm gì và đưa bằng chứng test thật.
- **Hỏi trước khi chốt design option** — đưa 2–3 lựa chọn kèm tradeoff, đừng tự quyết (trừ khi đã được uỷ quyền).
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

Quy mô hiện tại: 16 controller / 47 endpoint / 123 file Java / ~5.4k LOC. Test tự động: chỉ có `ManagementsystemApplicationTests` (context load) — **test nghiệp vụ vẫn là curl protocol thủ công**.

## 3) Trạng thái repo — ĐỌC TRƯỚC KHI LÀM

```
origin/main = local main = b5d31ef   Merge branch 'main' (sau PR #13)
working tree sạch
```

```zsh
cd ~/Code/Projects/english-center-project
git fetch --prune
git switch main && git pull --ff-only
git log --oneline -5      # dòng đầu phải là b5d31ef (hoặc commit HANDOFF mới nhất)
```

- Đã merge: `feature/b7-auth-login` (#10), `feature/b1b-create-user` (#11), `feature/c1-attendance` (#12), `feature/c2-enrollment-rules` (**#13**).
- Remote còn branch cũ (b1b, b2, b4, b5, b6, b7, c1, c2) — **không xoá**.
- `.dsh/` **đã ignore** trong `.gitignore`; skill của agent nằm ở `~/.dsh/skills` (user scope), **không commit**.
- Branch mới: `git switch -c feature/cX-ten-ngan` (luôn dùng đúng `feature/`).

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

### C2 — Enrollment rules (PR #13, merge 2f17895)
Endpoint mới/đổi:

| Method | Path | Ghi chú |
|---|---|---|
| GET | `/enrollments/{id}` | 404 nếu không có |
| PUT | `/enrollments/{id}` | **chỉ** đổi `enrolledDate` (`@Valid` → thiếu = 400) |
| PUT | `/enrollments/{id}/status` | `{"status": "WITHDRAWN"}` |

Rule đã chốt:
- Tạo enrollment: student phải `ACTIVE` (nếu không → 409 `Student with id X is INACTIVE. Only ACTIVE students can be enrolled.`); mỗi cặp (student, class) **chỉ 1 ACTIVE** (→ 409 `Student with id X already has an active enrollment in class Y.`).
- Chuyển trạng thái: `ACTIVE → WITHDRAWN | COMPLETED`; WITHDRAWN/COMPLETED là **terminal** (không quay lại ACTIVE — muốn học lại thì tạo enrollment mới). Cùng status → 409 `Enrollment N is already COMPLETED.`; sai hướng → 409 `Cannot change enrollment status from COMPLETED to ACTIVE.`
- Race 2 request đồng thời: DB unique chặn, `saveAndFlush` + catch `DataIntegrityViolationException` → vẫn 409 cùng message.

**DB constraint (quan trọng):** MySQL không có partial index, nên "chỉ 1 ACTIVE" được làm bằng generated column + unique:

```sql
ALTER TABLE enrollments
  ADD COLUMN active_flag TINYINT GENERATED ALWAYS AS (IF(status='ACTIVE',1,NULL)) STORED,
  ADD UNIQUE KEY uk_enrollment_active (student_id, class_id, active_flag);
```

- Cột `active_flag`: ACTIVE → 1, còn lại → NULL (NULL không đụng nhau) → học lại/nghỉ nhiều lần vẫn OK.
- Hibernate `ddl-auto=update` **tự tạo cột** nhưng **KHÔNG thêm unique key** lên bảng đã tồn tại → DB mới phải chạy SQL trên tay.
- Trước khi ALTER phải check không còn ACTIVE trùng: `SELECT student_id, class_id, COUNT(*) FROM enrollments WHERE status='ACTIVE' GROUP BY 1,2 HAVING COUNT(*)>1;` (có row là ALTER fail 1062).
- Test đã chạy thật: create/404/409, 400 validation, học lại sau COMPLETED = 200, 2 chu kỳ WITHDRAWN = 200, insert trùng ACTIVE bị 1062.

## 5) Quy ước code đang dùng

- Controller → Service → Repository. **Constructor injection**, field `private final`.
- **Không trả entity ra ngoài** — luôn DTO (`XxxResponseDto`), map bằng `private toDto` trong service.
- **1 DTO cho response** (list + detail dùng chung); **DTO riêng cho từng request body** — không gộp DTO "update chung" có field optional.
- Method ghi dữ liệu: **`@Transactional`**. Method đọc: không cần.
- So sánh `Long` bằng `.equals()`, **không** `==`; enum thì `==` được.
- Rule trạng thái đặt trong enum (`canMoveTo`), service check **same-status trước** rồi mới `canMoveTo`.
- Error mapping (`GlobalExceptionHandler`): `BusinessRuleException` → **409**, `NotFoundException` → **404**, `@Valid` → **400** + `errors:{field:message}`, `DataIntegrityViolationException` → 409 generic, JSON hỏng/enum sai → 400 `Malformed JSON request or invalid value.`
- Message: 404 **không** dấu chấm cuối (`Enrollment with id 5 not found`); 409/400 **có** dấu chấm.
- `@ManyToOne` mặc định **EAGER** — cẩn thận N+1 khi list lớn.

Enums hiện có:

```
Role             ADMIN, TEACHER, STUDENT
StudentStatus    ACTIVE, INACTIVE, ALUMNI
EnrollmentStatus ACTIVE, WITHDRAWN, COMPLETED   (+ canMoveTo)
AttendanceStatus PRESENT, ABSENT, LATE
InvoiceStatus    UNPAID, PARTIALLY_PAID, PAID
PaymentMethod    BANKING, CASH
PayrollStatus    DRAFT, CONFIRMED, PAID        (+ canMoveTo)
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
| GET | `/enrollments/{id}` |
| PUT | `/enrollments/{id}` (chỉ enrolledDate) |
| PUT | `/enrollments/{id}/status` |
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
2. **C3 — profile endpoints** (đề xuất làm tiếp): `GET /students/{id}`, `PUT /students/{id}`, `GET /teachers/{id}`, `PUT /teachers/{id}`.
   - Student sửa được: `fullName` (nằm ở `User`), `parentPhone`, `dateOfBirth`, `evaluationSheetUrl`. **Không** cho đổi `username`/role.
   - Teacher sửa được: `fullName`, `specialization`, `phone`.
   - Câu hỏi mở Q13: đổi `StudentStatus` (INACTIVE/ALUMNI) — gộp vào `PUT /students/{id}` hay tách `PUT /students/{id}/status` như enrollment?
3. **B7b** (cần Q3): TEACHER/STUDENT xem dữ liệu của mình.
4. **C8** (cần Q8): pagination.
5. Rồi: hardening billing/payroll, Flyway, Docker.

Backlog gốc còn **C5, C6, C7** — file gốc đã mất, session mới cần hỏi lại Sang trước khi làm.

## 8) Câu hỏi design (trạng thái)

| # | Câu hỏi | Trạng thái / đề xuất |
|---|---|---|
| Q3 | B7b: TEACHER xem payroll của mình? STUDENT xem invoice của mình? | **CHƯA CHỐT** — đề xuất: có, read-only qua `/me/...` |
| Q5 | Payment vượt số tiền còn lại của invoice | 409, không cho overpay |
| Q6 | Student `INACTIVE` | **ĐÃ CHỐT (C2)**: chặn ghi danh mới; chưa làm chặn login; không tự rút ghi danh cũ |
| Q7 | Session tạo tay có `teacherTaught=false`? | Có, mặc định `false` |
| Q8 | Pagination (C8) | Làm sau, chốt response `Page<T>` trước |
| Q9 | Docker hoá | Để cuối, trước khi nộp |
| Q11 | Rounding tiền | `HALF_UP`, scale 2 |
| Q12 | Scale cột DECIMAL | `DECIMAL(12,2)` (hiện Hibernate tạo `decimal(38,2)`) |
| Q13 | Đổi `StudentStatus` qua endpoint nào? | **CHƯA CHỐT** — đề xuất tách `PUT /students/{id}/status` |

## 9) Gotchas

- **MySQL phải chạy trước khi start app.** Kiểm tra: `brew services list | grep mysql`. (2026-11-03: đang **tắt**.)
- App ở port **8080**; chạy lại thì phải tắt trước, không thì `Port 8080 already in use`.
- `.env` **không commit** (`backend/.env` trong `.gitignore`); `.env.example` là mẫu. Key cần: `DB_USERNAME`, `DB_PASSWORD`, `APP_ADMIN_USERNAME`, `APP_ADMIN_PASSWORD`, `JWT_SECRET`, `SPRING_PROFILES_ACTIVE`.
- `JWT_SECRET` phải **≥ 32 bytes**, không thì app throw `IllegalStateException` lúc start.
- Token sống **480 phút**. Gặp 401 bất ngờ → login lại, đừng đoán.
- `ADMIN_TOKEN` trong `.env` hay hết hạn — luôn login mới mỗi lần test.
- `AdminSeeder` chỉ seed ADMIN khi DB **chưa có** admin nào.
- `ddl-auto=update` **không** tạo unique key mới trên bảng đã tồn tại (xem SQL mục 4) — và không phải cách làm production, sẽ cần Flyway.
- Profile `dev` bật `show-sql=true` → log rất dài.
- CORS chỉ cho `http://localhost:5173` và `http://localhost:3000`.
- DB local đang có: 3 student (id 1–3, đều ACTIVE), 5 enrollment (id 1 ACTIVE; id 2–5 WITHDRAWN — 3 row cuối là dữ liệu test C2 cũ, **đừng xoá** vì invoice/attendance đang trỏ vào id 1–4), admin + teacher1/2 + student1/2 từ seed cũ.
- Test xong phải **xoá dữ liệu test** vừa tạo.

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

# 7) Verify unique index enrollment còn đó
mysql -u "$DB_USERNAME" -p"$DB_PASSWORD" english_center -e "SHOW INDEX FROM enrollments;" | grep uk_enrollment_active
```

## 11) Quy trình làm 1 feature (đã áp dụng cho B1b, C1, C2)

1. **Preflight**: `git switch main && git pull --ff-only`; working tree sạch; tạo branch `feature/cX-...`.
2. Chốt design (hỏi Sang) → code → AI review **P0 → P1 → P2**.
3. **Viết test protocol trước khi chạy**, 4 nhóm:
   - **A** — happy path + validation (200/400/404/409)
   - **F** — filter/query
   - **U** — update
   - **Z** — auth (401/403)
   Mỗi case: *input → expected status → body mong đợi*. Chạy bằng `curl` thật, ghi lại kết quả thật.
4. **Dọn dữ liệu test** (xoá row vừa tạo) và kiểm tra lại DB.
5. Commit message rõ nghĩa (imperative), push, tạo PR base `main`, merge, `git switch main && git pull`.

## 12) Prompt gợi ý cho session mới

> Đọc `HANDOFF.md` trong repo. Xác nhận lại state: branch/commit hiện tại, việc đã xong, việc kế tiếp. **Chưa code gì cho tới khi tôi chọn việc.** Giữ đúng cách làm việc ở mục 0 — mặc định tôi viết code, bạn hướng dẫn và review theo P0/P1/P2.
