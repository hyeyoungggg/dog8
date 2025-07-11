package dev.mvc.contentsgood;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE contentsgood (
//    contentsgoodno  NUMBER(10)    NOT NULL PRIMARY KEY,
//    rdate         DATE    NOT NULL,
//    dogcontno      NUMBER(10)    NOT NULL,
//    usersno      NUMBER(10)    NOT NULL,
//      FOREIGN KEY (dogcontno) REFERENCES contents(dogcontno),
//      FOREIGN KEY (usersno) REFERENCES member(usersno)
//  );

@Getter @Setter @ToString
public class ContentsgoodVO {
  /** 컨텐츠 추천 번호 */
  private int contentsgoodno;
  
  /** 등록일 */
  private String rdate;
  
  /** 컨텐츠 번호 */
  private int dogcontno;
  
  /** 회원 번호 */
  private int usersno;

}



