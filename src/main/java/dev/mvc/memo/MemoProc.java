package dev.mvc.memo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dev.mvc.memo.memoProc")
public class MemoProc implements MemoProcInter {

  @Autowired
  private MemoDAOInter memoDAO;

  @Override
  public int create(MemoVO memoVO) {
    return memoDAO.create(memoVO);
  }

  @Override
  public MemoVO read(int memono) {
    return memoDAO.read(memono);
  }

  @Override
  public List<MemoVO> list() {
    return memoDAO.list();
  }

  @Override
  public int update(MemoVO memoVO) {
    return memoDAO.update(memoVO);
  }

  @Override
  public int delete(int memono) {
    return memoDAO.delete(memono);
  }
  
  @Override
  public List<MemoVO> list_by_latest() {
    return memoDAO.list_by_latest();
  }

  @Override
  public List<MemoVO> list_by_viewcnt() {
    return memoDAO.list_by_viewcnt();
  }
  
  @Override
  public int increaseViewcnt(int memono) {
    return memoDAO.increaseViewcnt(memono);
  }
  
  @Override
  public MemoVO prev(int memono) {
    return this.memoDAO.prev(memono);
  }

  @Override
  public MemoVO next(int memono) {
    return this.memoDAO.next(memono);
  }
  
}
