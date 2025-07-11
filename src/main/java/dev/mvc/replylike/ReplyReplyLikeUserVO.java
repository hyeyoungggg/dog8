package dev.mvc.replylike;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ReplyReplyLikeUserVO {
  /** 댓글 좋아요 번호 */
  private int replylikeno;

  /** 좋아요 등록일 */
  private String rdate;

  /** 댓글 번호 */
  private int replyno;

  /** 댓글 내용 */
  private String replyContent;

  /** 회원 번호 */
  private int usersno;

  /** 회원 아이디 */
  private String id;

  /** 회원 이름 */
  private String usersname;
}
