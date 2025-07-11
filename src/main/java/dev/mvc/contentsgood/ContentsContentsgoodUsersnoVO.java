package dev.mvc.contentsgood;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//-- 테이블 3개 join
//SELECT r.contentsgoodno, r.rdate, r.dogdontno, c.title as c_title, r.usersno, m.id, m.usersname
//FROM contents c, contentsgood r, member m
//WHERE c.dogdontno = r.dogdontno AND r.usersno = m.usersno
//ORDER BY contentsgoodno DESC;

@Getter @Setter @ToString
public class ContentsContentsgoodUsersnoVO {
  /** 컨텐츠 추천 번호 */
  private int contentsgoodno;
  
  /** 등록일 */
  private String rdate;
  
  /** 컨텐츠 번호 */
  private int dogcontno;
  
  /** 제목 */
  private String c_title = "";
  
  /** 회원 번호 */
  private int usersno;
  
  /** 아이디(이메일) */
  private String id = "";
  
  /** 회원 성명 */
  private String usersname = "";
  
}



