DROP TABLE replylike;

CREATE TABLE replylike (
    replylikeno   NUMBER(10)    NOT NULL,
    rdate         DATE DEFAULT SYSDATE NOT NULL,
    replyno       NUMBER(10)    NOT NULL,
    usersno       NUMBER(10)    NOT NULL,
    dogcontno        NUMBER(10)        NOT NULL,
    PRIMARY KEY (replylikeno),
    FOREIGN KEY (dogcontno) REFERENCES dogcont(dogcontno),
    FOREIGN KEY (replyno) REFERENCES reply(replyno),
    FOREIGN KEY (usersno) REFERENCES users(usersno)
);

-- 중복 좋아요 방지를 위한 제약조건
ALTER TABLE replylike ADD CONSTRAINT uq_reply_user UNIQUE (replyno, usersno);

DROP SEQUENCE replylike_seq;

CREATE SEQUENCE replylike_seq
START WITH 1
INCREMENT BY 1
MAXVALUE 9999999999
CACHE 2
NOCYCLE;

SELECT rl.replylikeno,
       TO_CHAR(rl.rdate, 'YYYY-MM-DD HH24:MI:SS') AS rdate,
       r.replyno,
       r.content AS replyContent,
       u.usersno,
       u.id,
       u.usersname
  FROM replylike rl
  JOIN reply r ON rl.replyno = r.replyno
  JOIN users u ON rl.usersno = u.usersno
 ORDER BY rl.replylikeno DESC;

-- 예시 유저
INSERT INTO users (usersno, id, userspasswd, usersname, usersgrade, created_at, updated_at)
VALUES (1, 'testuser', '1234', '테스트유저', 15, SYSDATE, SYSDATE);

-- 예시 댓글
INSERT INTO reply (replyno, dogcontno, usersno, content, rdate)
VALUES (1, 1, 1, '테스트 댓글입니다.', SYSDATE);

-- 예시 댓글 좋아요
INSERT INTO replylike (replylikeno, replyno, usersno, rdate)
VALUES (1, 1, 1, SYSDATE);

commit;
