package dev.mvc.Dog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.mvc.dog.DogDAOInter;
import dev.mvc.dog.DogVO;

@SpringBootTest
class DogApplicationTests {

  @Autowired
  private DogDAOInter dogDAOInter; // DogDAOInter를 사용하는 테스트 클래스

  @Test
  void contextLoads() {
    // 예시: DogVO 객체 생성 후 데이터베이스 연동 테스트
    DogVO dogVO = new DogVO();
    dogVO.setName("김123"); // 강아지 이름
    dogVO.setBreed("Shiba Inu"); // 강아지 품종
    // 강아지 등록
    dogDAOInter.create(dogVO);

    // 예시: 강아지 목록 조회
    // List<DogVO> dogList = dogDAOInter.list_all();
    // System.out.println(dogList);
  }
}
