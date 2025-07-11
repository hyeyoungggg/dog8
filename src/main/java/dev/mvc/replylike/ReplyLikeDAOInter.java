package dev.mvc.replylike;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ReplyLikeDAOInter {
  
  /**
   * 댓글 좋아요 등록
   * @param vo
   * @return 성공한 레코드 수
   */
  public int create(ReplyLikeVO vo);
  
  /**
   * 좋아요 시퀀스 번호 생성
   * @return 다음 시퀀스 값
   */
  public int getReplyLikeSeq();
  
  /**
   * 특정 댓글의 좋아요 수
   * @param replyno
   * @return 좋아요 수
   */
  public int countByReplyno(int replyno);

  /**
   * 해당 사용자가 특정 댓글에 좋아요 눌렀는지 확인
   * @param map replyno, usersno 포함
   * @return 1이면 눌렀음, 0이면 안 눌렀음
   */
  public int isLiked(Map<String, Object> map);

  /**
   * 좋아요 취소 (삭제)
   * @param map replyno, usersno 포함
   * @return 성공한 레코드 수
   */
  public int delete(Map<String, Object> map);
  
  /** ✅ 관리자용 전체 목록 조회 */
  public List<ReplyLikeVO> list_all();

  /** ✅ PK로 삭제 (관리자용) */
  public int deleteByPk(int replylikeno);
  
  public ArrayList<ReplyReplyLikeUserVO> list_all_join();

}
