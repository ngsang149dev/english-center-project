# HANDOFF — English Center Management System

> Context để một session AI mới hiểu và tiếp tục công việc. **Cập nhật: 2026-11-11.**
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
| DB | MySQL local Homebrew 26.7, database `english_center` |
| Build | Maven wrapper `./mvnw` |
| Auth | JWT HS256, stateless |
| Migration | **Flyway** (`backend/src/main/resources/db/migration`), `ddl-auto=validate` |

Package root `com.abuenglishcenter.managementsystem` — **package-by-feature** (mỗi feature 1 folder: entity + repo + service + controller + dto).

Quy mô hiện tại: 18 controller / 59 endpoint / 130 file Java / ~5.8k LOC. **Test tự động: 29 unit test / 8 file** (`./mvnw test`, không cần MySQL): rule trạng thái 3 enum, overpay guard, login INACTIVE/ALUMNI, page wrapper, remaining amount. Integration test + curl protocol vẫn thủ công.

## 3) Trạng thái repo — ĐỌC TRƯỚC KHI LÀM

```
origin/main = local main = 3d6b454   Merge PR #18 (C8 + review fixes)
PR #19 đang mở: feature/unit-tests — chờ merge
```

```zsh
cd ~/Code/Projects/english-center-project
git fetch --prune
git switch main && git pull --ff-only
git log --oneline -5      # dòng đầu phải là 3d6b454 (hoặc commit/merge #19)
```

- Đã merge: `#10` auth, `#11` create-user, `#12` attendance, `#13` enrollment rules, `#14` C3 profile (32dfb4d), `#15` B7b (abb046d), `#16` Q5/Q6/Q12/Flyway (fbf54fe).
- **Đang mở**: `#19` unit tests.
- Remote còn branch cũ (b1b, b2, b4, b5, b6, b7, c1, c2, c3, b7b) — **không xoá**.
- `.dsh/` **đã ignore** trong `.gitignore`; skill của agent nằm ở `~/.dsh/skills` (user scope), **không commit**.
- Branch mới: `git switch -c feature/cX-ten-ngan` (luôn dùng đúng `feature/`).

## 4) Đã xong (verified)

### B7 — Auth (PR #10)
- `POST /auth/login` public → `{accessToken, tokenType:"Bearer", expiresIn:28800, user}`; `GET /auth/me` authenticated.
- Claims: `sub` = user id, `role` = Role name.
- `JwtAuthenticationConverter` map claim `role` + prefix `ROLE_`.
- Lỗi security trả `application/problem+json`: `ProblemDetailSecurityHandlers` implements **cả** `AuthenticationEntryPoint` và `AccessDeniedHandler`, gắn **cả hai chỗ**: `exceptionHandling(...)` và `oauth2ResourceServer(...).authenticationEntryPoint(...)`.

### B1b — POST /users (PR #11)
- Chỉ tạo được **ADMIN**. TEACHER/STUDENT → **409** `"Only ADMIN accounts can be created here. Use POST /teachers or POST /students"`.
- `@NotNull role` → thiếu = **400** + `errors.role`; trùng username → **409**.

### C1 — Attendance (PR #12, b7fe29d)
- Unique `(session_id, enrollment_id)`; `POST /attendances` batch: chặn trùng trong request (409), trùng DB (409), enrollment khác lớp (409), 404 nếu session/enrollment không có; `@Transactional` → 1 item fail rollback cả batch.
- `GET /attendances?sessionId=&enrollmentId=` lọc 4 nhánh; không filter → `findAll()` (chờ C8).
- `PUT /attendances/{id}` chỉ đổi `status`.

### C2 — Enrollment rules (PR #13, 2f17895)
| Method | Path | Ghi chú |
|---|---|---|
| GET | `/enrollments/{id}` | 404 nếu không có |
| PUT | `/enrollments/{id}` | **chỉ** đổi `enrolledDate` (thiếu = 400) |
| PUT | `/enrollments/{id}/status` | `{"status": "WITHDRAWN"}` |

- Tạo enrollment: student phải `ACTIVE` (409 `Student with id X is INACTIVE. Only ACTIVE students can be enrolled.`); mỗi cặp (student, class) **chỉ 1 ACTIVE** (409 `Student with id X already has an active enrollment in class Y.`).
- `ACTIVE → WITHDRAWN | COMPLETED`; sau đó **terminal**. Cùng status → 409 `Enrollment N is already COMPLETED.`; sai hướng → `Cannot change enrollment status from COMPLETED to ACTIVE.`
- Race 2 request: DB unique chặn, `saveAndFlush` + catch `DataIntegrityViolationException` → 409 cùng message.

**DB constraint:** MySQL không có partial index → generated column + unique:

```sql
ALTER TABLE enrollments
  ADD COLUMN active_flag TINYINT GENERATED ALWAYS AS (IF(status='ACTIVE',1,NULL)) STORED,
  ADD UNIQUE KEY uk_enrollment_active (student_id, class_id, active_flag);
```

- ACTIVE → 1, còn lại → NULL (NULL không đụng nhau) → học lại/nghỉ nhiều lần OK.
- **Từ PR #16 constraint này nằm trong `V1__baseline.sql`** (DB cũ đã có sẵn, DB mới sẽ có khi chạy V1).
- Check trước khi ALTER: `SELECT student_id, class_id, COUNT(*) FROM enrollments WHERE status='ACTIVE' GROUP BY 1,2 HAVING COUNT(*)>1;`

### C3 — Profile endpoints (PR #14, 32dfb4d)
| Method | Path | Ghi chú |
|---|---|---|
| GET | `/students/{id}` | 404 nếu không có |
| PUT | `/students/{id}` | full-replace `fullName`, `parentPhone`, `dateOfBirth`, `evaluationSheetUrl`; bỏ field = xoá |
| PUT | `/students/{id}/status` | `{"status":"INACTIVE"}`, transitions: `ACTIVE ↔ INACTIVE`, cả hai → `ALUMNI`, ALUMNI terminal |
| GET | `/teachers/{id}` | 404 nếu không có |
| PUT | `/teachers/{id}` | `fullName`, `specialization`, `phone` |

- Không cho đổi `username`/`role`/`status` qua `PUT /students/{id}`.
- Phone: `@Pattern("\\+?[0-9 ]{8,15}")`.
- Đã verify: INACTIVE student bị chặn `POST /enrollments` (409).

### B7b — `/me` self-service (PR #15, abb046d)
| Method | Path | Role |
|---|---|---|
| GET | `/me/teacher` | TEACHER |
| GET | `/me/teacher/payrolls` | TEACHER |
| GET | `/me/teacher/sessions` | TEACHER |
| GET | `/me/student` | STUDENT |
| GET | `/me/student/enrollments` | STUDENT |
| GET | `/me/student/invoices` | STUDENT |
| GET | `/me/student/attendances` | STUDENT |

- **ID luôn lấy từ JWT `sub`**, không có param `?studentId=` → không IDOR. Profile resolve bằng `findByUserId`, 404 `Student profile for user N not found`.
- `SecurityConfig`: `/me/teacher/**` → TEACHER, `/me/student/**` → STUDENT, còn lại ADMIN.
- Verified: student khác gọi `/me/student/invoices` → `[]`; TEACHER gọi `/me/student` → 403; không token → 401.

### Q5/Q6/Q12 + Flyway (PR #16 — đang mở)
- **Q5 overpay**: `POST /payments` chặn khi `amount > adjustedAmount − sum(payments chưa cancel)` → **409** `Payment amount X exceeds the remaining balance Y of invoice Z.` Invoice thiếu `adjusted_amount` (data cũ) fallback về `amount`, không NPE.
- **Q6 login**: student `INACTIVE`/`ALUMNI` → **403** `Student account is INACTIVE. Only ACTIVE students can sign in.` (exception mới `ForbiddenException` → `@ExceptionHandler` 403). Sai mật khẩu vẫn **401**.
- **Q12**: 11 cột tiền → `DECIMAL(12,2)` ở cả entity lẫn DB.
- **Flyway**: `V1__baseline.sql` (schema đầy đủ, sinh từ DB thật) + `V2__money_decimal_12_2.sql`; `ddl-auto=validate`; `spring.flyway.baseline-on-migrate=true` → DB cũ được baseline ở version 1, không chạy lại CREATE TABLE.

### C8 — Pagination (PR #17)
Wrapper cố định cho **mọi list endpoint** (không trả `Page<T>` của Spring):

```json
{ "items": [...], "page": 0, "size": 20, "totalItems": 57, "totalPages": 3 }
```

- `common/PageResponseDto` (record) + `PageResponseDto.pageable(page, size)` → `PageRequest` với `Sort.by("id").descending()` (**mới nhất trước**).
- Query: `?page=0&size=20`; `page >= 0`, `1 <= size <= 100`; sai → **400** `errors.page` / `errors.size` (method validation của Spring, không cần `@Validated`).
- Áp cho 14 endpoint: `users`, `teachers`, `students`, `classes`, `class-schedules`, `class-sessions`, `class-fee-history`, `enrollments`, `attendances` (giữ filter `sessionId`/`enrollmentId`), `invoices` (giữ filter `studentId`), `payments`, `teacher-rates`, `teacher-payrolls`, `expenses`.
- `/me/...` và `/classes/{id}/schedules` vẫn trả **array** (dữ liệu của 1 người/lớp, không cần phân trang).
- `page` cap **1000** (`@Max(1000)`) → offset tối đa 1000×100 = 100k row, tránh deep-offset scan và tràn `int` của JPA.
- `/invoices?studentId=...` gọi `ensureStudentExists` → student không tồn tại = **404**, không trả page rỗng.
- Verified: `?size=2` → `totalPages=2`, `page=1` → 1 item, `page=5` → `items: []`, `size=500` → 400, `page=-1` → 400, `page=1001` → 400, `size=abc` → 400, `/invoices?studentId=999999` → 404, không token → 401.

### Unit tests (PR #19)
29 test / 8 file, thuần unit (JUnit 5 + Mockito + AssertJ), `./mvnw test` chạy ~1s, **không cần MySQL**:

| File | Nội dung |
|---|---|
| `EnrollmentStatusTest` (4) | ACTIVE → WITHDRAWN/COMPLETED; terminal; same-status |
| `StudentStatusTest` (3) | ACTIVE ↔ INACTIVE, cả hai → ALUMNI, ALUMNI terminal |
| `PayrollStatusTest` (3) | DRAFT → CONFIRMED → PAID, PAID → CONFIRMED |
| `PaymentServiceTest` (4) | overpay 1 cent = 409 đúng message, đúng bằng remaining = OK, invoice đã PAID = 409, invoice không tồn tại = 404 |
| `InvoiceServiceTest` (3) | remaining = adjusted − paid, fallback `amount` khi thiếu `adjustedAmount`, không âm |
| `AuthServiceTest` (6) | ACTIVE có token; INACTIVE/ALUMNI/thiếu profile = 403 và **không** phát token; sai password = 401; ADMIN bỏ qua check student |
| `StudentServiceTest` (3) | 409 same-status / 409 ALUMNI → ACTIVE, ACTIVE → ALUMNI OK |
| `PageResponseDtoTest` (3) | map items + totalItems/totalPages, page rỗng, cap size + sort id DESC |

**Breaking change (PR #17):** 14 list endpoint đổi từ JSON array → envelope `{items,...}`. Chưa có client nào (frontend làm sau, repo backend-only, deploy 1 instance) nên không cần route version. **Trước khi public API / có frontend**: chốt lại contract rồi mới freeze dưới `/api/v1`, và mọi thay đổi shape sau đó phải version hoặc deploy đồng bộ client.

## 5) Quy ước code đang dùng

- Controller → Service → Repository. **Constructor injection**, field `private final`.
- **Không trả entity ra ngoài** — luôn DTO (`XxxResponseDto`), map bằng `private toDto` trong service.
- **1 DTO cho response** (list + detail dùng chung); **DTO riêng cho từng request body** — không gộp DTO "update chung" có field optional.
- Method ghi dữ liệu: **`@Transactional`**. Method đọc: không cần.
- So sánh `Long` bằng `.equals()`, **không** `==`; enum thì `==` được.
- Rule trạng thái đặt trong enum (`canMoveTo`), service check **same-status trước** rồi mới `canMoveTo`.
- Error mapping (`GlobalExceptionHandler`): `BusinessRuleException` → **409**, `NotFoundException` → **404**, `ForbiddenException` → **403**, `@Valid` → **400** + `errors:{field:message}`, `DataIntegrityViolationException` → 409 generic, JSON hỏng/enum sai → 400 `Malformed JSON request or invalid value.`
- Message: 404 **không** dấu chấm cuối; 409/400/403 **có** dấu chấm.
- Tiền: `BigDecimal`, cột `DECIMAL(12,2)`, làm tròn `HALF_UP` scale 2 khi cần.
- List endpoint: trả `PageResponseDto<T>` (`items/page/size/totalItems/totalPages`), sort mặc định `id DESC`, `size` tối đa 100.
- `@ManyToOne` mặc định **EAGER** — cẩn thận N+1 khi list lớn.

Enums hiện có:

```
Role             ADMIN, TEACHER, STUDENT
StudentStatus    ACTIVE, INACTIVE, ALUMNI          (+ canMoveTo)
EnrollmentStatus ACTIVE, WITHDRAWN, COMPLETED       (+ canMoveTo)
AttendanceStatus PRESENT, ABSENT, LATE
InvoiceStatus    UNPAID, PARTIALLY_PAID, PAID
PaymentMethod    BANKING, CASH
PayrollStatus    DRAFT, CONFIRMED, PAID             (+ canMoveTo)
ExpenseCategory  RENT, UTILITIES, STAFF_SALARY, MARKETING, SUPPLIES, OTHER
```

## 6) Endpoint hiện có

Mặc định **ROLE_ADMIN**, trừ khi ghi khác. Các endpoint `GET` dạng list nhận `?page=&size=` và trả wrapper `{items,page,size,totalItems,totalPages}` (xem C8).

| Method | Path | Ghi chú |
|---|---|---|
| POST | `/auth/login` | public |
| GET | `/auth/me` | authenticated (mọi role) |
| GET, POST | `/users` | |
| GET, POST | `/teachers` | |
| GET, PUT | `/teachers/{id}` | |
| GET, POST | `/students` | |
| GET, PUT | `/students/{id}` | |
| PUT | `/students/{id}/status` | |
| GET, POST | `/classes` | |
| GET | `/classes/{id}/schedules` | |
| PUT | `/classes/{id}/teacher` | |
| GET, POST | `/class-schedules` | |
| GET, POST | `/class-sessions` | |
| PUT | `/class-sessions/{id}` | |
| PUT | `/class-sessions/{id}/teacher` | |
| GET, POST | `/class-fee-history` | |
| GET, POST | `/enrollments` | |
| GET | `/enrollments/{id}` | |
| PUT | `/enrollments/{id}` | chỉ enrolledDate |
| PUT | `/enrollments/{id}/status` | |
| GET, POST | `/attendances` | GET nhận `sessionId`, `enrollmentId` |
| PUT | `/attendances/{id}` | |
| GET, POST | `/invoices` | |
| GET | `/invoices/balance` | |
| POST | `/invoices/generate` | |
| PUT | `/invoices/{id}/amount` | |
| GET, POST | `/payments` | POST chặn overpay (409) |
| PUT | `/payments/{id}/cancel` | |
| GET, POST | `/teacher-rates` | |
| GET, POST | `/teacher-payrolls` | |
| PUT | `/teacher-payrolls/{id}/bonus` | |
| PUT | `/teacher-payrolls/{id}/status` | |
| POST | `/teacher-payrolls/{id}/recalculate` | |
| DELETE | `/teacher-payrolls/{id}` | |
| GET, POST | `/expenses` | |
| GET | `/reports/monthly` | |
| GET | `/me/teacher` + `/payrolls` + `/sessions` | TEACHER |
| GET | `/me/student` + `/enrollments` + `/invoices` + `/attendances` | STUDENT |

## 7) Việc tiếp theo

1. **Merge PR #19** rồi `git switch main && git pull --ff-only`.
2. **Integration test** (bước tiếp của test): `@SpringBootTest` + MySQL/Testcontainers cho vài luồng end-to-end (login → enroll → invoice → payment). Cần DB nên tách khỏi unit suite.
3. **Sort param** cho list (`?sort=field,asc|desc`) nếu frontend cần — hiện cố định id giảm dần.
4. Hardening khác: audit field (`createdAt`/`updatedAt`), Docker.
5. Backlog gốc còn **C5, C6, C7** — file gốc đã mất, phải hỏi lại Sang trước khi làm.

## 8) Câu hỏi design (trạng thái)

| # | Câu hỏi | Trạng thái |
|---|---|---|
| Q3 | B7b: TEACHER xem payroll của mình? STUDENT xem invoice? | **CHỐT: có** — read-only qua `/me/...` (PR #15) |
| Q5 | Payment vượt số tiền còn lại | **XONG (PR #16)**: 409, chặn overpay |
| Q6 | Student `INACTIVE` login | **XONG (PR #16)**: 403 khi login, ngoài việc đã chặn ghi danh (C2) |
| Q7 | Session tạo tay có `teacherTaught=false`? | Có, mặc định `false` |
| Q8 | Pagination (C8) | **XONG (PR #17)**: wrapper `{items,page,size,totalItems,totalPages}` |
| Q9 | Docker hoá | Để cuối, trước khi nộp |
| Q11 | Rounding tiền | `HALF_UP`, scale 2 |
| Q12 | Scale cột DECIMAL | **XONG (PR #16)**: `DECIMAL(12,2)` |
| Q13 | Đổi `StudentStatus` qua endpoint nào? | **CHỐT**: tách `PUT /students/{id}/status` (C3) |

## 9) Gotchas

- **MySQL phải chạy trước khi start app.** Kiểm tra: `brew services list | grep mysql`.
- App ở port **8080**; chạy lại phải tắt trước, không thì `Port 8080 already in use`.
- `.env` **không commit** (`backend/.env` trong `.gitignore`); `.env.example` là mẫu. Key: `DB_USERNAME`, `DB_PASSWORD`, `APP_ADMIN_USERNAME`, `APP_ADMIN_PASSWORD`, `JWT_SECRET`, `SPRING_PROFILES_ACTIVE`.
- `JWT_SECRET` phải **≥ 32 bytes**; token sống **480 phút** — gặp 401 thì login lại, đừng đoán.
- **Flyway (từ PR #16)**:
  - Schema do Flyway quản lý, `ddl-auto=validate` → đổi entity mà không thêm migration sẽ **fail lúc start** (`Schema-validation: missing column/table`).
  - Thêm cột/bảng: tạo `V{n}__mo_ta.sql` trong `db/migration`, **không sửa file V cũ** (checksum).
  - DB cũ đã được baseline ở version 1; **chưa test đường DB trắng** (user `english_app` không có quyền `CREATE DATABASE`). Muốn test: tạo DB mới bằng root, grant cho `english_app`, đổi `spring.datasource.url`, start app → phải chạy V1+V2 rồi boot.
  - User `english_app` **không có quyền `DROP`** → migration chứa `DROP TABLE` sẽ fail và để lại row `success=0` trong `flyway_schema_history`. Sửa: `DELETE FROM flyway_schema_history WHERE success=0;` rồi sửa/xoá file migration.
  - Còn rác schema cũ: bảng `classroom` (rỗng) và cột `users.fullname` (không map) — xoá tay bằng root khi rảnh.
  - Cần **`spring-boot-flyway`** (Boot 4 tách autoconfig theo module) chứ không chỉ `flyway-core`; thiếu nó thì `spring.flyway.*` bị bỏ qua im lặng.
- `AdminSeeder` chỉ seed ADMIN khi DB **chưa có** admin nào.
- Profile `dev` bật `show-sql=true` → log rất dài.
- CORS chỉ cho `http://localhost:5173` và `http://localhost:3000`.
- DB local hiện có: 3 student (id 1–3 ACTIVE), 2 teacher, 6 user, 5 enrollment (id 1 ACTIVE; 2–5 WITHDRAWN — **đừng xoá**, invoice/attendance trỏ vào id 1–4), 15 class_session, 6 invoice, 6 payment.
- Test xong phải **xoá dữ liệu test** vừa tạo; kiểm tra lại count.

## 10) Lệnh vận hành (macOS zsh)

```zsh
# 1) MySQL
brew services start mysql
brew services list | grep mysql          # phải là started

# 2) Compile + chạy app (nạp .env, không echo secret)
cd ~/Code/Projects/english-center-project/backend
./mvnw -q -DskipTests compile
( set -a; . ./.env; set +a; ./mvnw -q spring-boot:run ) > /tmp/app.log 2>&1 &
sleep 25 && tail -5 /tmp/app.log          # "Started ManagementsystemApplication"

# 3) Token + smoke test
set -a; . ./.env; set +a
TOKEN=$(curl -s -X POST localhost:8080/auth/login -H 'Content-Type: application/json' \
  -d "{\"username\":\"$APP_ADMIN_USERNAME\",\"password\":\"$APP_ADMIN_PASSWORD\"}" | jq -r .accessToken)
curl -s -o /dev/null -w '%{http_code}\n' localhost:8080/auth/me -H "Authorization: Bearer $TOKEN"   # 200

# 4) Tắt app
pkill -f spring-boot:run ; sleep 2
lsof -nP -iTCP:8080 -sTCP:LISTEN          # phải trống

# 5) Query DB
mysql -u "$DB_USERNAME" -p"$DB_PASSWORD" english_center \
  -e "SELECT id, username, role FROM users ORDER BY id;"

# 6) Kiểm tra Flyway + index enrollment
mysql -u "$DB_USERNAME" -p"$DB_PASSWORD" english_center -e "SELECT installed_rank,version,description,success FROM flyway_schema_history;"
mysql -u "$DB_USERNAME" -p"$DB_PASSWORD" english_center -e "SHOW INDEX FROM enrollments;" | grep uk_enrollment_active
```

## 11) Quy trình làm 1 feature (đã áp dụng cho B1b, C1, C2, C3, B7b, Q5/Q6/Q12)

1. **Preflight**: `git switch main && git pull --ff-only`; working tree sạch; tạo branch `feature/cX-...`.
2. Chốt design (hỏi Sang) → code → AI review **P0 → P1 → P2**.
3. **Viết test protocol trước khi chạy**, 4 nhóm:
   - **A** — happy path + validation (200/400/403/404/409)
   - **F** — filter/query
   - **U** — update
   - **Z** — auth (401/403)
   Mỗi case: *input → expected status → body mong đợi*. Chạy bằng `curl` thật, ghi lại kết quả thật.
4. **Dọn dữ liệu test** (xoá row vừa tạo) và kiểm tra lại DB bằng count.
5. Commit message rõ nghĩa (imperative), push, tạo PR base `main`, merge, `git switch main && git pull`.

## 12) Prompt gợi ý cho session mới

> Đọc `HANDOFF.md` trong repo. Xác nhận lại state: branch/commit hiện tại, việc đã xong, việc kế tiếp. **Chưa code gì cho tới khi tôi chọn việc.** Giữ đúng cách làm việc ở mục 0 — mặc định tôi viết code, bạn hướng dẫn và review theo P0/P1/P2.
