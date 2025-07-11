-- 테이블 구조
-- member 삭제전에 FK가 선언된 blog 테이블 먼저 삭제합니다.
DROP TABLE qna;
DROP TABLE reply;
DROP TABLE usrs;
-- 제약 조건과 함께 삭제(제약 조건이 있어도 삭제됨, 권장하지 않음.)
DROP TABLE users CASCADE CONSTRAINTS; 
 
CREATE TABLE users (
  usersno NUMBER(10) NOT NULL, -- 회원 번호, 레코드를 구분하는 컬럼 
  id         VARCHAR(30)   NOT NULL UNIQUE, -- 이메일(아이디), 중복 안됨, 레코드를 구분 
  userspasswd     VARCHAR(200)   NOT NULL, -- 패스워드, 영숫자 조합, 암호화
  usersname      VARCHAR(30)   NOT NULL, -- 성명, 한글 10자 저장 가능
  userstel            VARCHAR(14)   NOT NULL, -- 전화번호
  userszipcode     VARCHAR(5)        NULL, -- 우편번호, 12345
  usersaddress1    VARCHAR(80)       NULL, -- 주소 1
  usersaddress2    VARCHAR(50)       NULL, -- 주소 2
  usersdate       DATE             NOT NULL, -- 가입일    
  usersgrade        NUMBER(2)     NOT NULL, -- 등급(1~10: 관리자, 11~20: 회원, 40~49: 정지 회원, 99: 탈퇴 회원)
  PRIMARY KEY (usersno)               -- 한번 등록된 값은 중복 안됨
);

COMMENT ON TABLE USERS is '회원';
COMMENT ON COLUMN USERS.USERSNO is '회원 번호';
COMMENT ON COLUMN USERS.ID is '아이디';
COMMENT ON COLUMN USERS.UsersPASSWD is '패스워드';
COMMENT ON COLUMN USERS.UsersNAME is '성명';
COMMENT ON COLUMN USERS.UsersTEL is '전화번호';
COMMENT ON COLUMN USERS.UsersZIPCODE is '우편번호';
COMMENT ON COLUMN USERS.UsersADDRESS1 is '주소1';
COMMENT ON COLUMN USERS.UsersADDRESS2 is '주소2';
COMMENT ON COLUMN USERS.UsersDATE is '가입일';
COMMENT ON COLUMN USERS.UsersGRADE is '등급';

DROP SEQUENCE users_seq;

CREATE SEQUENCE users_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999 --> NUMBER(7) 대응
  CACHE 2                       -- 2번은 메모리에서만 계산
  NOCYCLE;                     -- 다시 1부터 생성되는 것을 방지
  
  1. 등록
 
1) id 중복 확인(null 값을 가지고 있으면 count에서 제외됨)
SELECT COUNT(id)
FROM users
WHERE id='user1';

SELECT COUNT(id) as cnt
FROM users
WHERE id='user1';

2) 등록
-- 회원 관리용 계정, Q/A 용 계정
INSERT INTO users(usersno, id, userspasswd, usersname, userstel, userszipcode,
                                usersaddress1, usersaddress2, usersdate, usersgrade)
VALUES (users_seq.nextval, 'admin', '1234', '통합 관리자', '000-0000-0000', '12345',
             '서울시 종로구', '관철동', sysdate, 1);
             
INSERT INTO users(usersno, id, userspasswd, usersname, userstel, userszipcode,
                                usersaddress1,usersaddress2, usersdate, usersgrade)
VALUES (users_seq.nextval, 'qnaadmin', '1234', '질문답변관리자', '000-0000-0000', '12345',
             '서울시 종로구', '관철동', sysdate, 1);
 
-- 개인 회원 테스트 계정
INSERT INTO users(usersno, id, userspasswd, usersname, userstel, userszipcode, usersaddress1, usersaddress2, usersdate, usersgrade)
VALUES (users_seq.nextval, 'user1@gmail.com', '1234', '왕눈이', '000-0000-0000', '12345', '서울시 종로구', '관철동', sysdate, 15);
 
INSERT INTO users(usersno, id, userspasswd, usersname, userstel, userszipcode, usersaddress1, usersaddress2, usersdate, usersgrade)
VALUES (users_seq.nextval, 'user2@gmail.com', '1234', '아로미', '000-0000-0000', '12345', '서울시 종로구', '관철동', sysdate, 15);
 
INSERT INTO users(usersno, id, userspasswd, usersname, userstel, userszipcode, usersaddress1, usersaddress2, usersdate, usersgrade)
VALUES (users_seq.nextval, 'user3@gmail.com', '1234', '투투투', '000-0000-0000', '12345', '서울시 종로구', '관철동', sysdate, 15);
 
-- 그룹별 공유 회원 기준
INSERT INTO users(usersno, id, userspasswd, usersname, userstel, userszipcode, usersaddress1, usersaddress2, usersdate, usersgrade)
VALUES (users_seq.nextval, 'team1', '1234', '개발팀', '000-0000-0000', '12345', '서울시 종로구', '관철동', sysdate, 15);
 
INSERT INTO users(usersno, id, userspasswd, usersname, userstel, userszipcode, usersaddress1, usersaddress2, usersdate, usersgrade)
VALUES (users_seq.nextval, 'team2', '1234', '웹퍼블리셔팀', '000-0000-0000', '12345', '서울시 종로구', '관철동', sysdate, 15);
 
INSERT INTO users(usersno, id, userspasswd, usersname, userstel, userszipcode, usersaddress1, usersaddress2, usersdate, usersgrade)
VALUES (users_seq.nextval, 'team3', '1234', '디자인팀', '000-0000-0000', '12345', '서울시 종로구', '관철동', sysdate, 15);

COMMIT;

2. 목록
- 검색을 하지 않는 경우, 전체 목록 출력
 
SELECT usersno, id, userspasswd, usersname, userstel, userszipcode, usersaddress1, usersaddress2, usersdate, usersgrade
FROM users
ORDER BY usersgrade ASC, id ASC;

3. 조회
 
1) 사원 정보 조회
SELECT usersno, id, userspasswd, usersname, userstel, userszipcode, usersaddress1, usersaddress2, usersdate, usersgrade
FROM users
WHERE usersno = 1;

SELECT usersno, id, userspasswd, usersname, userstel, userszipcode, usersaddress1, usersaddress2, usersdate, usersgrade
FROM users
WHERE id = 'user1@gmail.com';
 
   
4. 수정, PK: 변경 못함, UNIQUE: 변경을 권장하지 않음(id)
UPDATE users
SET usersname='왕눈이', userstel='111-1111-1111', userszipcode='00000',
    usersaddress1='서울시 종로구', usersaddress2='관철동', usersgrade=30
WHERE usersno=15;

COMMIT;

 
5. 삭제
1) 모두 삭제
DELETE FROM users;
 
2) 특정 회원 삭제
DELETE FROM users
WHERE usersno=12;

COMMIT;

 
6. 로그인
SELECT COUNT(usersno) as cnt
FROM users
WHERE id='user1@gmail.com' AND userspasswd='1234';
 cnt
 ---
   0
   
   
7. 패스워드 변경
1) 패스워드 검사
SELECT COUNT(usersno) as cnt
FROM users
WHERE usersno=1 AND userspasswd='1234';
 
2) 패스워드 수정
UPDATE users
SET userspasswd='0000'
WHERE usersno=1;

COMMIT;

8. 회원등급 변경

-- 정지회원
UPDATE users
SET usersgrade = 30
WHERE usersno = 5;

-- 탈퇴회원
UPDATE users
SET usersgrade = 40
WHERE usersno = 9;

INSERT INTO users(usersno, id, userspasswd, usersname, userstel, userszipcode,
                                usersaddress1, usersaddress2, usersdate, usersgrade)
VALUES (users_seq.nextval, 'admin', 'fS/kjO+fuEKk06Zl7VYMhg==', '통합 관리자', '000-0000-0000', '12345',
             '서울시 종로구', '관철동', sysdate, 1);

COMMIT;

SELECT usersno, id, userspasswd, usersname, userstel, userszipcode, usersaddress1, usersaddress2, usersdate, usersgrade
FROM users
ORDER BY usersgrade ASC, id ASC;
commit;