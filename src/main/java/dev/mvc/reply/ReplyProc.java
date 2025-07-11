package dev.mvc.reply;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dev.mvc.reply.ReplyProc")
public class ReplyProc implements ReplyProcInter {

  @Autowired
  private ReplyDAOInter replyDAO;

  @Override
  public int create(ReplyVO replyVO) {
    return this.replyDAO.create(replyVO);
  }

  @Override
  public List<ReplyVO> list() {
    return this.replyDAO.list();
  }

  @Override
  public List<ReplyVO> list_by_dogcontno(int dogcontno) {
    return this.replyDAO.list_by_dogcontno(dogcontno);
  }

  @Override
  public ReplyVO read(int replyno) {
    return this.replyDAO.read(replyno);
  }

  @Override
  public int update(ReplyVO replyVO) {
    return this.replyDAO.update(replyVO);
  }

  @Override
  public int delete(int replyno) {
    return this.replyDAO.delete(replyno);
  }

  @Override
  public int increaseReplycnt(int dogcontno) {
    return this.replyDAO.increaseReplycnt(dogcontno);
  }

  @Override
  public int decreaseReplycnt(int dogcontno) {
    return this.replyDAO.decreaseReplycnt(dogcontno);
  }
}
