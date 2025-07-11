package dev.mvc.memo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MemoProcInter {

  /**
   * 메모 등록
   * @param memoVO
   * @return 등록된 행 수
   */
  public int create(MemoVO memoVO);

  /**
   * 메모 1개 조회
   * @param memono
   * @return 메모 정보
   */
  public MemoVO read(int memono);

  /**
   * 메모 전체 목록 조회
   * @return 메모 목록
   */
  public List<MemoVO> list();

  /**
   * 메모 수정
   * @param memoVO
   * @return 수정된 행 수
   */
  public int update(MemoVO memoVO);

  /**
   * 메모 삭제
   * @param memono
   * @return 삭제된 행 수
   */
  public int delete(int memono);
  
  public List<MemoVO> list_by_latest();
  
  public List<MemoVO> list_by_viewcnt();
  
  public int increaseViewcnt(int memono);
  
  public MemoVO prev(int memono);  // 이전 메모
  
  public MemoVO next(int memono);  // 다음 메모

}
