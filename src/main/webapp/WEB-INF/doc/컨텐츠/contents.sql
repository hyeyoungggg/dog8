-- /src/main/webapp/WEB-INF/doc/컨텐츠/contents_c.sql
DROP TABLE dogcont CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능
DROP TABLE dogcont;

CREATE TABLE dogcont(
      dogcontno      NUMBER(10)     NOT NULL,           -- 컨텐츠 번호
      usersno        NUMBER(10)     NOT NULL,              -- 관리자 번호 (Users 테이블 참조 예상)
      dogid          NUMBER(10)     NOT NULL,              -- 카테고리 번호 (Dogs 테이블 참조 예상)
      dogname       VARCHAR2(100)  NOT NULL,
      breed_cont          VARCHAR2(100)  NOT NULL,              -- 견종
      dog_size       VARCHAR2(50),                         -- 견종 사이즈
      personality    VARCHAR2(500),                        -- 성격
      dogage           VARCHAR2(500),  
      recom          NUMBER(7)     DEFAULT 0,              -- 추천수
      cnt            NUMBER(7)     DEFAULT 0,              -- 조회수
      replycnt       NUMBER(7)     DEFAULT 0,              -- 댓글수
      passwd         VARCHAR2(100),                         -- 패스워드
      word           VARCHAR2(200),                        -- 검색어
      rdate          DATE               NOT NULL,                 -- 등록일
      file1          VARCHAR2(255),                        -- 메인 이미지
      file1saved     VARCHAR2(255),                        -- 실제 저장된 메인 이미지
      thumb1         VARCHAR2(255),                        -- 메인 이미지 Preview
      size1          NUMBER(10),                           -- 메인 이미지 크기
      map                                   VARCHAR2(1000)            NULL,
      youtube        VARCHAR2(1000),                        -- Youtube 영상
      mp4            VARCHAR2(255),                        -- 영상
      visible        CHAR(1) DEFAULT 'Y',                   -- 출력 모드 ('Y', 'N')
      PRIMARY KEY (dogcontno),
      FOREIGN KEY (usersno) REFERENCES users (usersno),
      FOREIGN KEY (dogid) REFERENCES dogs (dogid)
);

COMMENT ON TABLE dogcont is '컨텐츠 - 순례길';
COMMENT ON COLUMN dogcont.dogcontno is '컨텐츠 번호';
COMMENT ON COLUMN dogcont.usersno is '관리자 번호';
COMMENT ON COLUMN dogcont.dogid is '카테고리 번호';
COMMENT ON COLUMN dogcont.breed_cont is '견종';
COMMENT ON COLUMN dogcont.dog_size is '견종 사이즈';
COMMENT ON COLUMN dogcont.personality is '성격';
COMMENT ON COLUMN dogcont.dogname is '견종 이름';
COMMENT ON COLUMN dogcont.dogage is '견종 나이';
COMMENT ON COLUMN dogcont.recom is '추천수';
COMMENT ON COLUMN dogcont.cnt is '조회수';
COMMENT ON COLUMN dogcont.replycnt is '댓글수';
COMMENT ON COLUMN dogcont.passwd is '패스워드';
COMMENT ON COLUMN dogcont.word is '검색어';
COMMENT ON COLUMN dogcont.rdate is '등록일';
COMMENT ON COLUMN dogcont.file1 is '메인 이미지';
COMMENT ON COLUMN dogcont.file1saved is '실제 저장된 메인 이미지';
COMMENT ON COLUMN dogcont.thumb1 is '메인 이미지 Preview';
COMMENT ON COLUMN dogcont.size1 is '메인 이미지 크기';
COMMENT ON COLUMN dogcont.map is '지도';
COMMENT ON COLUMN dogcont.youtube is 'Youtube 영상';
COMMENT ON COLUMN dogcont.mp4 is '영상';
COMMENT ON COLUMN dogcont.visible is '출력 모드';

DROP SEQUENCE dogcont_seq;

CREATE SEQUENCE dogcont_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  
commit;

-- 등록 화면 유형 1: 커뮤니티(공지사항, 게시판, 자료실, 갤러리,  Q/A...)글 등록
INSERT INTO contents(contentsno, memberno, cateno, title, content, recom, cnt, replycnt, passwd, 
                     word, rdate, file1, file1saved, thumb1, size1)
VALUES(contents_seq.nextval, 9999, 100, '청계천 매화 거리', '제기동역에서 가까움 명품 산책로', 0, 0, 0, '123',
       '산책', sysdate, 'space.jpg', 'space_1.jpg', 'space_t.jpg', 1000);

-- 유형 1 전체 목록
SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, passwd, word, rdate,
           file1, file1saved, thumb1, size1
FROM contents
ORDER BY contentsno DESC;

-- 유형 2 카테고리별 목록
INSERT INTO dogcont (
    dogcontno, usersno, dogid, breed, description, recom, cnt, replycnt, passwd,
    word, rdate, file1, file1saved, thumb1, size1
) VALUES (
    dogcont_seq.nextval, 1, 2, '골든 리트리버', 
    '골든 리트리버는 사람 친화적인 성격으로 유명하며, 훈련이 용이한 강아지입니다. 그들은 매우 지능적이고, 운동 능력이 뛰어나며 가족들과 잘 지냅니다.', 
    0, 0, 0, '123',
    '골든 리트리버, 강아지, 견종, 애완견', sysdate, 
    'golden_retriever.jpg', 'golden_retriever_1.jpg', 'golden_retriever_thumb.jpg', 1000
);
            
INSERT INTO dogcont (
    dogcontno, usersno, dogid, breed, description, recom, cnt, replycnt, passwd,
    word, rdate, file1, file1saved, thumb1, size1
) VALUES (
    dogcont_seq.nextval, 1, 3, '시베리안 허스키', 
    '시베리안 허스키는 독특한 외모와 강한 체력, 높은 에너지로 유명합니다. 그들은 추운 기후에 잘 적응하며, 자주 운동이 필요합니다. 충성심이 강하고, 훈련이 잘 되지만 때때로 고집스러울 수 있습니다.', 
    0, 0, 0, '123',
    '시베리안 허스키, 강아지, 견종, 운동량 많은 강아지', sysdate, 
    'husky.jpg', 'husky_1.jpg', 'husky_thumb.jpg', 1000
);

INSERT INTO dogcont (
    dogcontno, usersno, dogid, breed, description, recom, cnt, replycnt, passwd,
    word, rdate, file1, file1saved, thumb1, size1
) VALUES (
    dogcont_seq.nextval, 1, 4, '비숑 프리제', 
    '비숑 프리제는 작은 체구와 풍성한 털로 유명한 강아지입니다. 그들은 매우 사회적이고, 사람과 잘 지내며, 다른 동물들과도 잘 어울립니다. 또한, 그들은 주인의 마음을 잘 읽고 따르는 성격을 가집니다.', 
    0, 0, 0, '123',
    '비숑 프리제, 강아지, 견종, 작은 강아지', sysdate, 
    'bichon_frise.jpg', 'bichon_frise_1.jpg', 'bichon_frise_thumb.jpg', 1000
);

COMMIT;

-- 전체 목록
SELECT dogcontno, usersno, dogid, breed, description, dog_size, personality,
       recom, cnt, replycnt, passwd, word, rdate,
       file1, file1saved, thumb1, size1, youtube, mp4, visible
FROM dogcont
ORDER BY dogcontno DESC;


-- 1번 cateno 만 출력
SELECT dogcontno, usersno, dogid, breed, description, dog_size, personality,
       recom, cnt, replycnt, passwd, word, rdate,
       LOWER(file1) AS file1, file1saved, thumb1, size1, youtube, mp4
FROM dogcont
WHERE dogid = 1
ORDER BY dogcontno DESC;

-- 2번 cateno 만 출력
SELECT dogcontno, usersno, dogid, breed, description, dog_size, personality,
       recom, cnt, replycnt, passwd, word, rdate,
       LOWER(file1) AS file1, file1saved, thumb1, size1, youtube, mp4
FROM dogcont
WHERE dogid = 2
ORDER BY dogcontno ASC;

-- 3번 cateno 만 출력
SELECT dogcontno, usersno, dogid, breed, description, dog_size, personality,
       recom, cnt, replycnt, passwd, word, rdate,
       LOWER(file1) AS file1, file1saved, thumb1, size1, youtube, mp4
FROM dogcont
WHERE dogid = 3
ORDER BY dogcontno ASC;


-- 모든 레코드 삭제
DELETE FROM dogcont;
commit;

-- 삭제
DELETE FROM dogcont
WHERE dogcontno = 25;
commit;

DELETE FROM dogcont
WHERE dogid=12 AND dogcontno <= 41;

commit;


-- ----------------------------------------------------------------------------------------------------
-- 검색, cateno별 검색 목록
-- ----------------------------------------------------------------------------------------------------
-- 모든글
SELECT dogcontno, usersno, dogid, breed, description, dog_size, personality,
       recom, cnt, replycnt, word, rdate,
       file1, file1saved, thumb1, size1, youtube, mp4
FROM dogcont
ORDER BY dogcontno ASC;

-- 카테고리별 목록
SELECT dogcontno, usersno, dogid, breed, description, dog_size, personality,
       recom, cnt, replycnt, word, rdate,
       file1, file1saved, thumb1, size1, youtube, mp4
FROM dogcont
WHERE dogid = 2
ORDER BY dogcontno ASC;


-- 1) 검색
-- ① cateno별 검색 목록
-- word 컬럼의 존재 이유: 검색 정확도를 높이기 위하여 중요 단어를 명시
-- 글에 'swiss'라는 단어만 등장하면 한글로 '스위스'는 검색 안됨.
-- 이런 문제를 방지하기위해 'swiss,스위스,스의스,수의스,유럽' 검색어가 들어간 word 컬럼을 추가함.
SELECT dogcontno, usersno, dogid, breed, description, dog_size, personality,
       recom, cnt, replycnt, word, rdate,
       file1, file1saved, thumb1, size1, youtube, mp4
FROM dogcont
WHERE dogid = 8 AND word LIKE '%부대찌게%'
ORDER BY dogcontno DESC;

-- title, content, word column search
SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, word, rdate,
           file1, file1saved, thumb1, size1, map, youtube
FROM contents
WHERE cateno=8 AND (title LIKE '%부대찌게%' OR content LIKE '%부대찌게%' OR word LIKE '%부대찌게%')
ORDER BY contentsno DESC;

-- ② 검색 레코드 갯수
-- 전체 레코드 갯수, 집계 함수
SELECT COUNT(*)
FROM contents
WHERE cateno=8;

  COUNT(*)  <- 컬럼명
----------
         5
         
SELECT COUNT(*) as cnt -- 함수 사용시는 컬럼 별명을 선언하는 것을 권장
FROM contents
WHERE cateno=8;

       CNT <- 컬럼명
----------
         5

-- cateno 별 검색된 레코드 갯수
SELECT COUNT(*) as cnt
FROM contents
WHERE cateno=8 AND word LIKE '%부대찌게%';

SELECT COUNT(*) as cnt
FROM contents
WHERE cateno=8 AND (title LIKE '%부대찌게%' OR content LIKE '%부대찌게%' OR word LIKE '%부대찌게%');

-- SUBSTR(컬럼명, 시작 index(1부터 시작), 길이), 부분 문자열 추출
SELECT contentsno, SUBSTR(title, 1, 4) as title
FROM contents
WHERE cateno=8 AND (content LIKE '%부대%');

-- SQL은 대소문자를 구분하지 않으나 WHERE문에 명시하는 값은 대소문자를 구분하여 검색
SELECT contentsno, title, word
FROM contents
WHERE cateno=8 AND (word LIKE '%FOOD%');

SELECT contentsno, title, word
FROM contents
WHERE cateno=8 AND (word LIKE '%food%'); 

SELECT contentsno, title, word
FROM contents
WHERE cateno=8 AND (LOWER(word) LIKE '%food%'); -- 대소문자를 일치 시켜서 검색

SELECT contentsno, title, word
FROM contents
WHERE cateno=8 AND (UPPER(word) LIKE '%' || UPPER('FOOD') || '%'); -- 대소문자를 일치 시켜서 검색 ★

SELECT contentsno, title, word
FROM contents
WHERE cateno=8 AND (LOWER(word) LIKE '%' || LOWER('Food') || '%'); -- 대소문자를 일치 시켜서 검색

SELECT contentsno || '. ' || title || ' 태그: ' || word as title -- 컬럼의 결합, ||
FROM contents
WHERE cateno=8 AND (LOWER(word) LIKE '%' || LOWER('Food') || '%'); -- 대소문자를 일치 시켜서 검색


SELECT UPPER('한글') FROM dual; -- dual: 오라클에서 SQL 형식을 맞추기위한 시스템 테이블

-- ----------------------------------------------------------------------------------------------------
-- 검색 + 페이징 + 메인 이미지
-- ----------------------------------------------------------------------------------------------------
-- step 1
SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube
FROM contents
WHERE cateno=1 AND (title LIKE '%단풍%' OR content LIKE '%단풍%' OR word LIKE '%단풍%')
ORDER BY contentsno DESC;

-- step 2
SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube, rownum as r
FROM (
          SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
                     file1, file1saved, thumb1, size1, map, youtube
          FROM contents
          WHERE cateno=1 AND (title LIKE '%단풍%' OR content LIKE '%단풍%' OR word LIKE '%단풍%')
          ORDER BY contentsno DESC
);

-- step 3, 1 page
SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube, r
FROM (
           SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
                      file1, file1saved, thumb1, size1, map, youtube, rownum as r
           FROM (
                     SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
                                file1, file1saved, thumb1, size1, map, youtube
                     FROM contents
                     WHERE cateno=1 AND (title LIKE '%단풍%' OR content LIKE '%단풍%' OR word LIKE '%단풍%')
                     ORDER BY contentsno DESC
           )          
)
WHERE r >= 1 AND r <= 3;

-- step 3, 2 page
SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube, r
FROM (
           SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
                      file1, file1saved, thumb1, size1, map, youtube, rownum as r
           FROM (
                     SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
                                file1, file1saved, thumb1, size1, map, youtube
                     FROM contents
                     WHERE cateno=1 AND (title LIKE '%단풍%' OR content LIKE '%단풍%' OR word LIKE '%단풍%')
                     ORDER BY contentsno DESC
           )          
)
WHERE r >= 4 AND r <= 6;

-- 대소문자를 처리하는 페이징 쿼리
SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube, r
FROM (
           SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
                      file1, file1saved, thumb1, size1, map, youtube, rownum as r
           FROM (
                     SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
                                file1, file1saved, thumb1, size1, map, youtube
                     FROM contents
                     WHERE cateno=1 AND (UPPER(title) LIKE '%' || UPPER('단풍') || '%' 
                                         OR UPPER(content) LIKE '%' || UPPER('단풍') || '%' 
                                         OR UPPER(word) LIKE '%' || UPPER('단풍') || '%')
                     ORDER BY contentsno DESC
           )          
)
WHERE r >= 1 AND r <= 3;

-- ----------------------------------------------------------------------------
-- 조회
-- ----------------------------------------------------------------------------
SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, passwd, word, rdate,
           file1, file1saved, thumb1, size1, map, youtube
FROM contents
WHERE contentsno = 1;

-- ----------------------------------------------------------------------------
-- 다음 지도, MAP, 먼저 레코드가 등록되어 있어야함.
-- map                                   VARCHAR2(1000)         NULL ,
-- ----------------------------------------------------------------------------
-- MAP 등록/수정
UPDATE contents SET map='카페산 지도 스크립트' WHERE contentsno=1;

-- MAP 삭제
UPDATE contents SET map='' WHERE contentsno=1;

commit;

-- ----------------------------------------------------------------------------
-- Youtube, 먼저 레코드가 등록되어 있어야함.
-- youtube                                   VARCHAR2(1000)         NULL ,
-- ----------------------------------------------------------------------------
-- youtube 등록/수정
UPDATE contents SET youtube='Youtube 스크립트' WHERE contentsno=1;

-- youtube 삭제
UPDATE contents SET youtube='' WHERE contentsno=1;

commit;

-- 패스워드 검사, id="password_check"
SELECT COUNT(*) as cnt 
FROM contents
WHERE contentsno=1 AND passwd='123';

-- 텍스트 수정: 예외 컬럼: 추천수, 조회수, 댓글 수
UPDATE contents
SET title='기차를 타고', content='계획없이 여행 출발',  word='나,기차,생각' 
WHERE contentsno = 2;

-- ERROR, " 사용 에러
UPDATE contents
SET title='기차를 타고', content="계획없이 '여행' 출발",  word='나,기차,생각'
WHERE contentsno = 1;

-- ERROR, \' 에러
UPDATE contents
SET title='기차를 타고', content='계획없이 \'여행\' 출발',  word='나,기차,생각'
WHERE contentsno = 1;

-- SUCCESS, '' 한번 ' 출력됨.
UPDATE contents
SET title='기차를 타고', content='계획없이 ''여행'' 출발',  word='나,기차,생각'
WHERE contentsno = 1;

-- SUCCESS
UPDATE contents
SET title='기차를 타고', content='계획없이 "여행" 출발',  word='나,기차,생각'
WHERE contentsno = 1;

commit;

-- 파일 수정
UPDATE contents
SET file1='train.jpg', file1saved='train.jpg', thumb1='train_t.jpg', size1=5000
WHERE contentsno = 1;

-- 삭제
DELETE FROM contents
WHERE contentsno = 42;

commit;

DELETE FROM contents
WHERE contentsno >= 7;

commit;

-- 추천
UPDATE contents
SET recom = recom + 1
WHERE contentsno = 1;

-- cateno FK 특정 그룹에 속한 레코드 갯수 산출
SELECT COUNT(*) as cnt 
FROM contents 
WHERE cateno=1;

-- memberno FK 특정 관리자에 속한 레코드 갯수 산출
SELECT COUNT(*) as cnt 
FROM contents 
WHERE memberno=1;

-- cateno FK 특정 그룹에 속한 레코드 모두 삭제
DELETE FROM contents
WHERE cateno=1;

-- memberno FK 특정 관리자에 속한 레코드 모두 삭제
DELETE FROM contents
WHERE memberno=1;

commit;

-- 다수의 카테고리에 속한 레코드 갯수 산출: IN
SELECT COUNT(*) as cnt
FROM contents
WHERE cateno IN(1,2,3);

-- 다수의 카테고리에 속한 레코드 모두 삭제: IN
SELECT contentsno, memberno, cateno, title
FROM contents
WHERE cateno IN(1,2,3);

CONTENTSNO    ADMINNO     CATENO TITLE                                                                                                                                                                                                                                                                                                       
---------- ---------- ---------- ------------------------
         3             1                   1           인터스텔라                                                                                                                                                                                                                                                                                                  
         4             1                   2           드라마                                                                                                                                                                                                                                                                                                      
         5             1                   3           컨저링                                                                                                                                                                                                                                                                                                      
         6             1                   1           마션       
         
SELECT contentsno, memberno, cateno, title
FROM contents
WHERE cateno IN('1','2','3');

CONTENTSNO    ADMINNO     CATENO TITLE                                                                                                                                                                                                                                                                                                       
---------- ---------- ---------- ------------------------
         3             1                   1           인터스텔라                                                                                                                                                                                                                                                                                                  
         4             1                   2           드라마                                                                                                                                                                                                                                                                                                      
         5             1                   3           컨저링                                                                                                                                                                                                                                                                                                      
         6             1                   1           마션       

-- ----------------------------------------------------------------------------------------------------
-- cate + contents INNER JOIN
-- ----------------------------------------------------------------------------------------------------
-- 모든글
SELECT c.name,
       t.contentsno, t.memberno, t.cateno, t.title, t.content, t.recom, t.cnt, t.replycnt, t.word, t.rdate,
       t.file1, t.file1saved, t.thumb1, t.size1, t.map, t.youtube
FROM cate c, contents t
WHERE c.cateno = t.cateno
ORDER BY t.contentsno DESC;

-- contents, member INNER JOIN
SELECT t.contentsno, t.memberno, t.cateno, t.title, t.content, t.recom, t.cnt, t.replycnt, t.word, t.rdate,
       t.file1, t.file1saved, t.thumb1, t.size1, t.map, t.youtube,
       a.mname
FROM member a, contents t
WHERE a.memberno = t.memberno
ORDER BY t.contentsno DESC;

SELECT t.contentsno, t.memberno, t.cateno, t.title, t.content, t.recom, t.cnt, t.replycnt, t.word, t.rdate,
       t.file1, t.file1saved, t.thumb1, t.size1, t.map, t.youtube,
       a.mname
FROM member a INNER JOIN contents t ON a.memberno = t.memberno
ORDER BY t.contentsno DESC;

-- ----------------------------------------------------------------------------------------------------
-- View + paging
-- ----------------------------------------------------------------------------------------------------
CREATE OR REPLACE VIEW vcontents
AS
SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, word, rdate,
        file1, file1saved, thumb1, size1, map, youtube
FROM contents
ORDER BY contentsno DESC;
                     
-- 1 page
SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
       file1, file1saved, thumb1, size1, map, youtube, r
FROM (
     SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
            file1, file1saved, thumb1, size1, map, youtube, rownum as r
     FROM vcontents -- View
     WHERE cateno=14 AND (title LIKE '%야경%' OR content LIKE '%야경%' OR word LIKE '%야경%')
)
WHERE r >= 1 AND r <= 3;

-- 2 page
SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
       file1, file1saved, thumb1, size1, map, youtube, r
FROM (
     SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
            file1, file1saved, thumb1, size1, map, youtube, rownum as r
     FROM vcontents -- View
     WHERE cateno=14 AND (title LIKE '%야경%' OR content LIKE '%야경%' OR word LIKE '%야경%')
)
WHERE r >= 4 AND r <= 6;


-- ----------------------------------------------------------------------------------------------------
-- 관심 카테고리의 좋아요(recom) 기준, 1번 회원이 1번 카테고리를 추천 받는 경우, 추천 상품이 7건일 경우
-- ----------------------------------------------------------------------------------------------------
SELECT contentsno, memberno, cateno, title, thumb1, r
FROM (
           SELECT contentsno, memberno, cateno, title, thumb1, rownum as r
           FROM (
                     SELECT contentsno, memberno, cateno, title, thumb1
                     FROM contents
                     WHERE cateno=1
                     ORDER BY recom DESC
           )          
)
WHERE r >= 1 AND r <= 7;

-- ----------------------------------------------------------------------------------------------------
-- 관심 카테고리의 평점(score) 기준, 1번 회원이 1번 카테고리를 추천 받는 경우, 추천 상품이 7건일 경우
-- ----------------------------------------------------------------------------------------------------
SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube, r
FROM (
           SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
                      file1, file1saved, thumb1, size1, map, youtube, rownum as r
           FROM (
                     SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
                                file1, file1saved, thumb1, size1, map, youtube
                     FROM contents
                     WHERE cateno=1
                     ORDER BY score DESC
           )          
)
WHERE r >= 1 AND r <= 7;

-- ----------------------------------------------------------------------------------------------------
-- 관심 카테고리의 최신 상품 기준, 1번 회원이 1번 카테고리를 추천 받는 경우, 추천 상품이 7건일 경우
-- ----------------------------------------------------------------------------------------------------
SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube, r
FROM (
           SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
                      file1, file1saved, thumb1, size1, map, youtube, rownum as r
           FROM (
                     SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
                                file1, file1saved, thumb1, size1, map, youtube
                     FROM contents
                     WHERE cateno=1
                     ORDER BY rdate DESC
           )          
)
WHERE r >= 1 AND r <= 7;

-- ----------------------------------------------------------------------------------------------------
-- 관심 카테고리의 조회수 높은 상품기준, 1번 회원이 1번 카테고리를 추천 받는 경우, 추천 상품이 7건일 경우
-- ----------------------------------------------------------------------------------------------------
SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube, r
FROM (
           SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
                      file1, file1saved, thumb1, size1, map, youtube, rownum as r
           FROM (
                     SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
                                file1, file1saved, thumb1, size1, map, youtube
                     FROM contents
                     WHERE cateno=1
                     ORDER BY cnt DESC
           )          
)
WHERE r >= 1 AND r <= 7;

-- ----------------------------------------------------------------------------------------------------
-- 관심 카테고리의 낮은 가격 상품 추천, 1번 회원이 1번 카테고리를 추천 받는 경우, 추천 상품이 7건일 경우
-- ----------------------------------------------------------------------------------------------------
SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube, r
FROM (
           SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
                      file1, file1saved, thumb1, size1, map, youtube, rownum as r
           FROM (
                     SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
                                file1, file1saved, thumb1, size1, map, youtube
                     FROM contents
                     WHERE cateno=1
                     ORDER BY price ASC
           )          
)
WHERE r >= 1 AND r <= 7;

-- ----------------------------------------------------------------------------------------------------
-- 관심 카테고리의 높은 가격 상품 추천, 1번 회원이 1번 카테고리를 추천 받는 경우, 추천 상품이 7건일 경우
-- ----------------------------------------------------------------------------------------------------
SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
           file1, file1saved, thumb1, size1, map, youtube, r
FROM (
           SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
                      file1, file1saved, thumb1, size1, map, youtube, rownum as r
           FROM (
                     SELECT contentsno, memberno, cateno, title, content, recom, cnt, replycnt, rdate,
                                file1, file1saved, thumb1, size1, map, youtube
                     FROM contents
                     WHERE cateno=1
                     ORDER BY price DESC
           )          
)
WHERE r >= 1 AND r <= 7;

-----------------------------------------------------------
-- FK cateno 컬럼에 대응하는 필수 SQL
-----------------------------------------------------------
-- 특정 카테고리에 속한 레코드 갯수를 리턴
SELECT COUNT(*) as cnt 
FROM contents 
WHERE cateno=1;
  
-- 특정 카테고리에 속한 모든 레코드 삭제
DELETE FROM contents
WHERE cateno=1;

-----------------------------------------------------------
-- FK memberno 컬럼에 대응하는 필수 SQL
-----------------------------------------------------------
-- 특정 회원에 속한 레코드 갯수를 리턴
SELECT COUNT(*) as cnt 
FROM contents 
WHERE memberno=1;
  
-- 특정 회원에 속한 모든 레코드 삭제
DELETE FROM contents
WHERE memberno=1;

1) 댓글수 증가
UPDATE contents
SET replycnt = replycnt + 1
WHERE contentsno = 3;

2) 댓글수 감소
UPDATE contents
SET replycnt = replycnt - 1
WHERE contentsno = 3;   

commit;
