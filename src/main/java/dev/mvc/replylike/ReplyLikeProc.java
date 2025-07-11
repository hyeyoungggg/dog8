package dev.mvc.replylike;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.replylike.ReplyLikeProc")
public class ReplyLikeProc implements ReplyLikeProcInter {

  @Autowired
  ReplyLikeDAOInter replyLikeDAO;

  @Override
  public int create(ReplyLikeVO vo) {
    return this.replyLikeDAO.create(vo);
  }

  @Override
  public int getReplyLikeSeq() {
    return this.replyLikeDAO.getReplyLikeSeq();
  }

  @Override
  public int countByReplyno(int replyno) {
    return this.replyLikeDAO.countByReplyno(replyno);
  }

  @Override
  public int isLiked(Map<String, Object> map) {
    return this.replyLikeDAO.isLiked(map);
  }

  @Override
  public int delete(Map<String, Object> map) {
    return this.replyLikeDAO.delete(map);
  }

  @Override
  public ArrayList<ReplyLikeVO> list_all() {
    return new ArrayList<>(this.replyLikeDAO.list_all());
  }

  @Override
  public int deleteByPk(int replylikeno) {
    return this.replyLikeDAO.deleteByPk(replylikeno);
  }
  
  @Override
  public ArrayList<ReplyReplyLikeUserVO> list_all_join() {
    return new ArrayList<>(this.replyLikeDAO.list_all_join());
  }


}
