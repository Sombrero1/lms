# БД:
![схема](db.jpg) \n
Основные запросы:
1. Авторизация: SELECT password FROM users WHERE id = ?
2. Все курсы: SELECT * FROM courses ORDER BY rating DESC. Индекс на таблицу: CREATE INDEX idx_courses ON courses USING btree (title, rating, category);
3. Курсы пользователя: SELECT * FROM coures WHERE course_id IN ( SELECT course_id FROM users_courses WHERE user_id = ?)
4. Модули курса: SELECT * FROM modules WHERE course_id = ? Индекс: CREATE INDEX idx_modules ON modules USING btree (name, author_create);
5. Топики модуля: SELECT * FROM topics WHERE module_id = ? Индекс: CREATE INDEX idx_topics ON modules USING btree (name, author_create);
6. Добавление курса пользователем к себе: INSERT INTO users_courses VALUES(?, ?)
7. Создание топика: INSERT INTO topics VALUES(?,...,?)
