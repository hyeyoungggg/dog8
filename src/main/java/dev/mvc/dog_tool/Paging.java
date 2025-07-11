package dev.mvc.dog_tool;
public class Paging {
  public static String createPaging(String url, int total, int nowPage, int recordPerPage, int pagePerBlock, String sort) {
    StringBuffer str = new StringBuffer();

    int totalPage = (int)(Math.ceil((double) total / recordPerPage));
    int totalGrp = (int)(Math.ceil((double) totalPage / pagePerBlock));
    int nowGrp = (int)(Math.ceil((double) nowPage / pagePerBlock));
    int startPage = ((nowGrp - 1) * pagePerBlock) + 1;
    int endPage = (nowGrp * pagePerBlock);

    if (endPage > totalPage) endPage = totalPage;

    String link = url + "?sort=" + sort + "&now_page=";

    str.append("<div style='text-align: center; font-family: Nanum Pen Script; font-size: 18px; margin: 20px;'>");

    if (nowGrp >= 2) {
      int prev = (nowGrp - 1) * pagePerBlock;
      str.append("<a href='" + link + prev + "'>이전</a> ");
    }

    for (int i = startPage; i <= endPage; i++) {
      if (i == nowPage) {
        str.append("<strong style='color: tomato;'>" + i + "</strong> ");
      } else {
        str.append("<a href='" + link + i + "'>" + i + "</a> ");
      }
    }

    if (nowGrp < totalGrp) {
      int next = (nowGrp * pagePerBlock) + 1;
      str.append("<a href='" + link + next + "'>다음</a>");
    }

    str.append("</div>");

    return str.toString();
  }
}
