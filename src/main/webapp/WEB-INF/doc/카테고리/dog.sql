-- 테이블 삭제 (필요시)
DROP TABLE Dogs CASCADE CONSTRAINTS;

-- 강아지 품종 테이블 생성
CREATE TABLE Dogs (
    dogid NUMBER(10) NOT NULL PRIMARY KEY,  -- 강아지 ID (Primary Key)
    name VARCHAR2(100) NOT NULL,             -- 강아지 이름
    breed VARCHAR2(500) NOT NULL,            -- 품종
    content VARCHAR2(500),               -- 품종 설명
    rdate  Date    NOT NULL,  -- 생성일
    VISIBLE  CHAR(1)      DEFAULT 'N'    NOT NULL,
    SEQNO  NUMBER(5)     DEFAULT 1     NOT NULL
);

DROP SEQUENCE dog;

CREATE SEQUENCE dog
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지         
       

-- 테이블 설명 추가
COMMENT ON TABLE Dogs IS '강아지 품종 정보';
COMMENT ON COLUMN Dogs.dogid IS '강아지 ID';
COMMENT ON COLUMN Dogs.name IS '강아지 이름';
COMMENT ON COLUMN Dogs.breed IS '강아지 품종';
COMMENT ON COLUMN Dogs.content IS '강아지 품종 설명';
COMMENT ON COLUMN Dogs.rdate IS '등록 일시';
COMMENT ON COLUMN cate.seqno is '출력 순서';
COMMENT ON COLUMN cate.visible is '출력 모드';

-- 샘플 데이터 삽입
INSERT INTO Dogs (dogid, name, breed, content, rdate, seqno, visible)
VALUES (1, 'abc', 'abc', 'abc',  SYSDATE, 1,'Y');

INSERT INTO Dogs (dogid, name, breed, content, rdate, seqno, visible)
VALUES (2, 'abc1', 'abc1', 'abc1',  SYSDATE, 1,'Y');

INSERT INTO Dogs (dogid, name, breed, content, rdate, seqno, visible)
VALUES (3, 'abc2', 'abc2', 'abc2',  SYSDATE, 1,'Y');

commit;

-- 목록
SELECT dogid, name, breed, content, rdate, seqno, visible
FROM Dogs
ORDER BY dogid ASC;


-- 데이터 조회
SELECT dogid, name, breed, content, rdate, seqno, visible
FROM Dogs
WHERE dogid=1;

--> UPDATE
UPDATE Dogs 
SET name='zhs', breed='adw', content='abc' , rdate = SYSDATE,seqno=1, visible = 'Y'
WHERE dogid=3;

--> DELECT
DELETE FROM dogs WHERE dogid = 3;

-- 출력 우선순위 낮춤
UPDATE dogs SET seqno=seqno+1 WHERE dogid=9;

SELECT dogid, name, breed, content, rdate, seqno, visible
FROM Dogs
ORDER BY dogid ASC;

-- 출력 우선순위 높임
UPDATE dogs SET seqno=seqno-1 WHERE dogid=9;

SELECT dogid, name, breed, content, rdate, seqno, visible
FROM Dogs
ORDER BY dogid ASC;

-- 카테고리 공개 설정
UPDATE dogs SET visible='Y' WHERE dogid=1;

-- 카테고리 비공개 설정 
UPDATE dogs SET visible='N' WHERE dogid=1;

COMMIT;

-- 카테고리 공개 설정
UPDATE dogs 
SET visible='Y' 
WHERE dogid=1;

-- 카테고리 비공개 설정 
UPDATE cate 
SET visible='N' 
WHERE dogid=1;

commit;

-- 회원/비회원에게 공개할 카테고리 그룹(대분류) 목록
SELECT dogid, name, breed, content, rdate, seqno, visible 
FROM dogs 
ORDER BY seqno ASC;

SELECT dogid, name, breed, content, rdate, seqno, visible 
FROM dogs 
WHERE name = '--' 
ORDER BY seqno ASC;
-- 공개된 대분류(*)만 출력
SELECT dogid, name, breed, content, rdate, seqno, visible
FROM dogs 
WHERE name = '--' AND visible = 'Y' 
ORDER BY seqno ASC;

-- 회원/비회원에게 공개할 카테고리(중분류) 목록(*)
SELECT dogid, name, breed, content, rdate, seqno, visible
FROM dogs 
WHERE grp='개발' AND visible = 'Y' 
ORDER BY seqno ASC;

SELECT dogid, name, breed, content, rdate, seqno, visible FROM dogs 
WHERE breed='개발' AND name != '--' AND visible = 'Y' 
ORDER BY seqno ASC;

SELECT dogid, name, breed, content, rdate, seqno, visible FROM dogs 
WHERE breed='여행' AND name != '--' AND visible = 'Y' 
ORDER BY seqno ASC;

commit;

-- 검색
SELECT dogid, name, breed, content, rdate, seqno, visible
FROM dogs
WHERE (UPPER(breed) LIKE '%' || UPPER('포메리안') || '%') OR (UPPER(name) LIKE '%' || UPPER('포메리안') || '%')
ORDER BY seqno ASC;

-- '카테고리 그룹'을 제외한 경우
SELECT dogid, name, breed, content, rdate, seqno, visible
FROM cate
WHERE (name != '--') AND ((UPPER(breed) LIKE '%' || UPPER('까페') || '%') OR (UPPER(name) LIKE '%' || UPPER('까페') || '%'))
ORDER BY seqno ASC;

        
-- 검색 갯수
SELECT COUNT(*) as cnt
FROM dogs
WHERE (UPPER(breed) LIKE '%' || UPPER('까페') || '%') OR (UPPER(name) LIKE '%' || UPPER('까페') || '%')
ORDER BY seqno ASC;

commit;

-- -----------------------------------------------------------------------------
-- 페이징: 정렬 -> ROWNUM -> 분할
-- -----------------------------------------------------------------------------
-- ① 정렬
SELECT dogid, name, breed, content, rdate, seqno, visible
FROM dogs
WHERE (UPPER(breed) LIKE '%' || UPPER('푸들') || '%') OR (UPPER(name) LIKE '%' || UPPER('까페') || '%')
ORDER BY seqno ASC;

-- ② 정렬 -> ROWNUM
SELECT dogid, name, breed, content, rdate, seqno, visible, rownum as r
FROM (
    SELECT dogid, name, breed, content, rdate, seqno, visible
    FROM dogs
    WHERE (UPPER(breed) LIKE '%' || UPPER('푸들') || '%') OR (UPPER(name) LIKE '%' || UPPER('말라뮤트') || '%')
    ORDER BY seqno ASC
);

-- ③ 정렬 -> ROWNUM -> 분할
SELECT dogid, name, breed, content, rdate, seqno, visible, rdate, r
FROM (
    SELECT dogid, name, breed, content, rdate, seqno, visible, rownum as r
    FROM (
        SELECT dogid, name, breed, content, rdate, seqno, visible
        FROM cate
        WHERE (UPPER(grp) LIKE '%' || UPPER('푸들') || '%') OR (UPPER(name) LIKE '%' || UPPER('말라뮤트') || '%')
        ORDER BY seqno ASC
    )
)
WHERE r >= 1 AND r <= 3;

    CATENO grp                NAME                                  CNT      SEQNO V RDATE                        R
---------- -------------------- ------------------------------ ---------- ---------- - ------------------- ----------
         8 까페                 --                                      0          1 Y 2024-09-13 10:04:04          1
        10 까페                 강화도2                                 0         10 Y 2024-09-24 05:42:54          2
        12 까페                 김포                                    0         11 Y 2024-09-19 04:19:50          3
        
SELECT dogid, name, breed, content, rdate, seqno, visible, r
FROM (
    SELECT dogid, name, breed, content, rdate, seqno, visible, rownum as r
    FROM (
        SELECT dogid, name, breed, content, rdate, seqno, visible
        FROM dogs
        WHERE (UPPER(breed) LIKE '%' || UPPER('말라뮤트') || '%') OR (UPPER(name) LIKE '%' || UPPER('말라뮤트') || '%')
        ORDER BY seqno ASC
    )
)
WHERE r >= 4 AND r <= 6;

    CATENO grp                NAME                                  CNT      SEQNO V RDATE                        R
---------- -------------------- ------------------------------ ---------- ---------- - ------------------- ----------
        15 까페                 추천                                    0         12 Y 2024-09-19 04:20:21          4
        17 까페                 남한산성                                0         15 Y 2024-09-24 04:01:35          5
        18 까페                 영종도                                  0         16 Y 2024-09-24 04:02:56          6

SELECT dogid, name, breed, content, rdate, seqno, visible, r
FROM (
    SELECT dogid, name, breed, content, rdate, seqno, visible, rownum as r
    FROM (
        SELECT dogid, name, breed, content, rdate, seqno, visiblee
        FROM dogs
        WHERE (UPPER(breed) LIKE '%' || UPPER('말라뮤트') || '%') OR (UPPER(name) LIKE '%' || UPPER('말라뮤트') || '%')
        ORDER BY seqno ASC
    )
)
WHERE r >= 7 AND r <= 9;

    CATENO grp                NAME                                  CNT      SEQNO V RDATE                        R
---------- -------------------- ------------------------------ ---------- ---------- - ------------------- ----------
        19 까페                 빵까페                                  0         19 Y 2024-09-24 04:08:50          7

commit;