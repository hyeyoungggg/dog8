package dev.mvc.replylike;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ReplyLikeVO {
  /** 댓글 좋아요 고유 번호 */
  private int replylikeno;

  /** 좋아요가 눌린 댓글 번호 */
  private int replyno;

  /** 좋아요를 누른 회원 번호 */
  private int usersno;

  /** 좋아요 등록일 */
  private String rdate;
  
  /** 컨텐츠 번호 */
  private int dogcontno;
}
