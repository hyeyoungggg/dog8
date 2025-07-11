package dev.mvc.replylike;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ReplyLikeProcInter {

  /**
   * 좋아요 등록
   * @param vo
   * @return 성공한 레코드 수
   */
  public int create(ReplyLikeVO vo);

  /**
   * 시퀀스 번호 생성
   * @return 다음 시퀀스 값
   */
  public int getReplyLikeSeq();

  /**
   * 특정 댓글에 눌린 좋아요 수
   * @param replyno
   * @return 좋아요 수
   */
  public int countByReplyno(int replyno);

  /**
   * 사용자가 특정 댓글에 좋아요를 눌렀는지 확인
   * @param map (replyno, usersno)
   * @return 1(눌렀음) or 0(안 눌렀음)
   */
  public int isLiked(Map<String, Object> map);

  /**
   * 좋아요 삭제 (취소)
   * @param map (replyno, usersno)
   * @return 성공한 레코드 수
   */
  public int delete(Map<String, Object> map);
  
  /** ✅ 전체 좋아요 목록 (관리자용) */
  public List<ReplyLikeVO> list_all();

  /** ✅ PK로 삭제 (관리자용) */
  public int deleteByPk(int replylikeno);
  
  public ArrayList<ReplyReplyLikeUserVO> list_all_join();

}