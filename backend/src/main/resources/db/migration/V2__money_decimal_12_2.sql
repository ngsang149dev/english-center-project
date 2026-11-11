-- Q12: money columns use DECIMAL(12,2) (max 9,999,999,999.99).
ALTER TABLE class_fee_history MODIFY COLUMN monthly_fee decimal(12,2) DEFAULT NULL;
ALTER TABLE expenses MODIFY COLUMN amount decimal(12,2) DEFAULT NULL;
ALTER TABLE invoices MODIFY COLUMN adjusted_amount decimal(12,2) DEFAULT NULL,
                        MODIFY COLUMN amount decimal(12,2) DEFAULT NULL;
ALTER TABLE payments MODIFY COLUMN amount decimal(12,2) DEFAULT NULL;
ALTER TABLE payroll_details MODIFY COLUMN rate_applied decimal(12,2) DEFAULT NULL,
                            MODIFY COLUMN subtotal decimal(12,2) DEFAULT NULL;
ALTER TABLE teacher_payrolls MODIFY COLUMN bonus decimal(12,2) DEFAULT NULL,
                             MODIFY COLUMN session_pay decimal(12,2) DEFAULT NULL,
                             MODIFY COLUMN total_pay decimal(12,2) DEFAULT NULL;
ALTER TABLE teacher_rates MODIFY COLUMN rate_per_session decimal(12,2) DEFAULT NULL;
