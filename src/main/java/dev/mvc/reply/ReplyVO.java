package dev.mvc.reply;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ReplyVO {
  /** 댓글 번호 */
  private int replyno;

  /** 관련 글 번호 */
  private int dogcontno;
  
  /** 회원 번호 */
  private int usersno;
  
  /** 내용 */
  private String content;
  
  /** 등록일 */
  private String rdate;
  
  private String usersname; // ← users 테이블에서 JOIN한 작성자 이름
  
  /** 좋아요 개수 */
  private int likeCount;  // ← 이 줄 추가
  
  private boolean liked;
  
}