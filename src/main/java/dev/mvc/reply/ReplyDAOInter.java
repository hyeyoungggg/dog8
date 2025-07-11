package dev.mvc.reply;

import java.util.List;

public interface ReplyDAOInter {

  /**
   * 댓글 등록
   * @param replyVO
   * @return 등록된 행 개수
   */
  public int create(ReplyVO replyVO);

  /**
   * 전체 댓글 목록 (replyno 역순)
   * @return 댓글 리스트
   */
  public List<ReplyVO> list();

  /**
   * 특정 글(dogcontno)의 댓글 목록
   * @param dogcontno
   * @return 댓글 리스트
   */
  public List<ReplyVO> list_by_dogcontno(int dogcontno);

  /**
   * 댓글 상세 조회
   * @param replyno
   * @return ReplyVO
   */
  public ReplyVO read(int replyno);

  /**
   * 댓글 내용 수정
   * @param replyVO
   * @return 수정된 행 개수
   */
  public int update(ReplyVO replyVO);

  /**
   * 댓글 삭제
   * @param replyno
   * @return 삭제된 행 개수
   */
  public int delete(int replyno);

  /**
   * 댓글 수 증가 (dogdont 테이블의 replycnt 필드 증가)
   * @param contentsno
   * @return 업데이트된 행 수
   */
  public int increaseReplycnt(int dogcontno);

  /**
   * 댓글 수 감소 (dogdont 테이블의 replycnt 필드 감소)
   * @param contentsno
   * @return 업데이트된 행 수
   */
  public int decreaseReplycnt(int dogcontno);
  
}
