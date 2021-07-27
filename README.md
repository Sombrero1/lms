# БД:
![схема](db.jpg) \n
Основные запросы:
1. Авторизация: SELECT password FROM users WHERE user_id = ?
2. Все курсы: SELECT * FROM courses ORDER BY rating DESC. Индекс на таблицу: CREATE INDEX idx_courses_title ON courses USING btree title; CREATE INDEX idx_courses_rating ON courses USING btree rating, CREATE INDEX idx_courses_category ON courses USING btree category
3. Курсы пользователя: SELECT * FROM coures WHERE course_id IN ( SELECT course_id FROM users_courses WHERE user_id = ?) 
4. Модули курса: SELECT * FROM modules WHERE course_id = ? Индекс: CREATE INDEX idx_modules ON modules USING btree name; CREATE INDEX idx_modules ON modules USING btree author_create; CREATE INDEX idx_modules ON modules USING btree name. Возможно стоит сделать и на course_id (т.к. курсов не так много, как модулей по отношению к топикам)
5. Топики модуля: SELECT * FROM topics WHERE module_id = ? Индексы на таблицу topics аналогичные как для предыдущей
6. Добавление курса пользователем к себе: INSERT INTO users_courses VALUES(?, ?)
7. Создание топика: INSERT INTO topics VALUES(?,...,?)

Сгенерированные DDL:
CREATE TABLE "users" (
	"user_id" serial NOT NULL,
	"username" varchar(100) NOT NULL,
	"password" varchar(255) NOT NULL,
	CONSTRAINT "users_pk" PRIMARY KEY ("user_id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "user_information" (
	"user_id" serial NOT NULL,
	"full_name" varchar(100) NOT NULL,
	"email" varchar(100) NOT NULL,
	"avatar_image" bytea NOT NULL,
	"registration" TIMESTAMP NOT NULL,
	"update" TIMESTAMP NOT NULL,
	"author_of_update" integer NOT NULL,
	"admin" BOOLEAN NOT NULL,
	CONSTRAINT "user_information_pk" PRIMARY KEY ("user_id")
) WITH (
  OIDS=FALSE
);

ALTER TABLE "user_information" ADD CONSTRAINT "user_information_fk0" FOREIGN KEY ("user_id") REFERENCES "users"("user_id");
ALTER TABLE "user_information" ADD CONSTRAINT "user_information_fk1" FOREIGN KEY ("author_of_update") REFERENCES "users"("user_id");


CREATE TABLE "categories" (
	"name" VARCHAR(100) NOT NULL,
	CONSTRAINT "categories_pk" PRIMARY KEY ("name")
) WITH (
  OIDS=FALSE
);


CREATE TABLE "courses" (
	"course_id" serial NOT NULL,
	"name" VARCHAR(100) NOT NULL,
	"description" bytea NOT NULL,
	"rating" DECIMAL NOT NULL,
	"author_created" serial NOT NULL,
	"author_of_update" serial NOT NULL,
	"author_of_delete" serial NOT NULL,
	"create" TIMESTAMP NOT NULL,
	"update" TIMESTAMP NOT NULL,
	"delete" TIMESTAMP NOT NULL,
	"tag" varchar(100) NOT NULL,
	"category" varchar(100) NOT NULL,
	CONSTRAINT "courses_pk" PRIMARY KEY ("course_id")
) WITH (
  OIDS=FALSE
);

ALTER TABLE "courses" ADD CONSTRAINT "courses_fk0" FOREIGN KEY ("author_created") REFERENCES "users"("user_id");
ALTER TABLE "courses" ADD CONSTRAINT "courses_fk1" FOREIGN KEY ("author_of_update") REFERENCES "users"("user_id");
ALTER TABLE "courses" ADD CONSTRAINT "courses_fk2" FOREIGN KEY ("author_of_delete") REFERENCES "users"("user_id");
ALTER TABLE "courses" ADD CONSTRAINT "courses_fk3" FOREIGN KEY ("category") REFERENCES "categories"("name");


CREATE TABLE "scores" (
	"user_id" integer NOT NULL,
	"course_id" integer NOT NULL,
	"score" integer NOT NULL
) WITH (
  OIDS=FALSE
);

ALTER TABLE "scores" ADD CONSTRAINT "scores_fk0" FOREIGN KEY ("user_id") REFERENCES "users"("user_id");
ALTER TABLE "scores" ADD CONSTRAINT "scores_fk1" FOREIGN KEY ("course_id") REFERENCES "courses"("course_id");






CREATE TABLE "user_courses" (
	"user_id" integer NOT NULL,
	"course_id" integer NOT NULL
) WITH (
  OIDS=FALSE
);

ALTER TABLE "user_courses" ADD CONSTRAINT "user_courses_fk0" FOREIGN KEY ("user_id") REFERENCES "users"("user_id");
ALTER TABLE "user_courses" ADD CONSTRAINT "user_courses_fk1" FOREIGN KEY ("course_id") REFERENCES "courses"("course_id");






CREATE TABLE "modules" (
	"course_id" integer NOT NULL,
	"module_id" serial NOT NULL,
	"name" varchar(100) NOT NULL,
	"description" bytea NOT NULL,
	"author_created" serial NOT NULL,
	"author_of_update" integer NOT NULL,
	"author_of_deleted" integer NOT NULL,
	"create" TIMESTAMP NOT NULL,
	"update" TIMESTAMP NOT NULL,
	"delete" TIMESTAMP NOT NULL,
	CONSTRAINT "modules_pk" PRIMARY KEY ("module_id")
) WITH (
  OIDS=FALSE
);
ALTER TABLE "modules" ADD CONSTRAINT "modules_fk0" FOREIGN KEY ("course_id") REFERENCES "courses"("course_id");
ALTER TABLE "modules" ADD CONSTRAINT "modules_fk1" FOREIGN KEY ("author_created") REFERENCES "users"("user_id");
ALTER TABLE "modules" ADD CONSTRAINT "modules_fk2" FOREIGN KEY ("author_of_update") REFERENCES "users"("user_id");
ALTER TABLE "modules" ADD CONSTRAINT "modules_fk3" FOREIGN KEY ("author_of_deleted") REFERENCES "users"("user_id");


CREATE TABLE "topics" (
	"topic_id" serial NOT NULL,
	"module_id" integer NOT NULL,
	"name" varchar(100) NOT NULL,
	"description" bytea NOT NULL,
	"author_created" integer NOT NULL,
	"author_updated" integer NOT NULL,
	"author_deleted" integer NOT NULL,
	"create" TIMESTAMP NOT NULL,
	"update" TIMESTAMP NOT NULL,
	"delete" TIMESTAMP NOT NULL,
	CONSTRAINT "topics_pk" PRIMARY KEY ("topic_id")
) WITH (
  OIDS=FALSE
);

ALTER TABLE "topics" ADD CONSTRAINT "topics_fk0" FOREIGN KEY ("module_id") REFERENCES "modules"("module_id");
ALTER TABLE "topics" ADD CONSTRAINT "topics_fk1" FOREIGN KEY ("author_created") REFERENCES "users"("user_id");
ALTER TABLE "topics" ADD CONSTRAINT "topics_fk2" FOREIGN KEY ("author_updated") REFERENCES "users"("user_id");
ALTER TABLE "topics" ADD CONSTRAINT "topics_fk3" FOREIGN KEY ("author_deleted") REFERENCES "users"("user_id");






CREATE TABLE "content" (
	"content_id" serial NOT NULL,
	"topic_id" serial NOT NULL,
	"type" varchar(100) NOT NULL,
	"content" serial NOT NULL,
	CONSTRAINT "content_pk" PRIMARY KEY ("content_id")
) WITH (
  OIDS=FALSE
);




ALTER TABLE "content" ADD CONSTRAINT "content_fk0" FOREIGN KEY ("topic_id") REFERENCES "topics"("topic_id");

















