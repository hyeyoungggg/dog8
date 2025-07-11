package dev.mvc.memo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemoVO {

  /** 메모 번호 (PK) */
  private int memono = 0;

  /** 메모 제목 */
  private String title = "";

  /** 메모 내용 */
  private String content = "";

  /** 등록일 */
  private Date rdate = null;

  /** 등록한 관리자 번호 (FK) */
  private int usersno = 0;

  /** 등록된 이전 제목 */
  private String old_title = "";

  /** 등록된 이전 내용 */
  private String old_content = "";

  /** 검색 키워드 저장 */
  private String keyword = "";

  /** 이동할 주소 저장 */
  private String url_address = "";
  
  private int viewcnt; // ← 반드시 추가
}
