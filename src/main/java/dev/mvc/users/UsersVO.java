package dev.mvc.users;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class UsersVO {
  /*
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
  */

    /** 회원 번호 */
    private int usersno;
    /** 아이디(이메일) */
    private String id = "";
    /** 패스워드 */
    private String userspasswd = "";
    /** 회원 성명 */
    private String usersname = "";
    /** 전화 번호 */
    private String userstel = "";
    /** 우편 번호 */
    private String userszipcode = "";
    /** 주소 1 */
    private String usersaddress1 = "";
    /** 주소 2 */
    private String usersaddress2 = "";
    /** 가입일 */
    private String usersdate = "";
    /** 등급 */
    private int usersgrade = 0;

    /** 등록된 패스워드 */
    private String old_passwd = "";
    /** id 저장 여부 */
    private String id_save = "";
    /** passwd 저장 여부 */
    private String passwd_save = "";
    /** 이동할 주소 저장 */
    private String url_address = "";


}