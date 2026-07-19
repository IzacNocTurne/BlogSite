package com.example.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TemporarySeedController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ReferralLogRepository referralLogRepository;

    @GetMapping("/seed-data")
    public String seedData() {
        // 1. 기존 데이터 삭제
        referralLogRepository.deleteAll();
        postRepository.deleteAll();

        // 2. 카테고리 확보
        Category redevelopment = categoryRepository.findByName("재개발");
        if (redevelopment == null) {
            redevelopment = new Category();
            redevelopment.setName("재개발");
            categoryRepository.save(redevelopment);
        }

        Category reconstruction = categoryRepository.findByName("재건축");
        if (reconstruction == null) {
            reconstruction = new Category();
            reconstruction.setName("재건축");
            categoryRepository.save(reconstruction);
        }

        List<Post> postsToAdd = new ArrayList<>();

        // --- 재개발 (Redevelopment) 10개 ---
        postsToAdd.add(createPost("중구 충무로 1~5구역, 녹지 생태 도심으로 대변신", 
            "2026년 3월 정비계획이 가결된 충무로 일대는 낙후된 인쇄소 밀집 지역을 벗어나 '직·주·락' 복합도심으로 조성됩니다. 을지로에서 퇴계로까지 연결되는 녹지 축이 핵심입니다.", redevelopment));
        
        postsToAdd.add(createPost("강북구 미아동 75번지, 최고 45층 1,600세대 확정", 
            "신속통합기획을 통해 미아사거리역 역세권에 대단지 아파트가 들어섭니다. 강북권 주거 환경 개선의 새로운 모델로 기대를 모으고 있습니다.", redevelopment));
        
        postsToAdd.add(createPost("노량진 뉴타운, 2027년 전 구역 착공 목표 속도전", 
            "노량진 2구역이 2026년 초 착공하며 뉴타운 전체가 활기를 띠고 있습니다. 2031년까지 한강변에 1만 세대 규모의 신도시급 주거지가 완성될 예정입니다.", redevelopment));
        
        postsToAdd.add(createPost("용산 한남3구역, 2026년 조기 착공 집중 지원", 
            "서울시의 주택 공급 전략사업지로 선정된 한남3구역은 행정 절차 간소화를 통해 2026년 내 조기 착공을 목표로 순항 중입니다.", redevelopment));
        
        postsToAdd.add(createPost("광진구 자양4동 A구역, 한강변 2,999세대 랜드마크", 
            "성수동과 인접한 자양4동 A구역이 최고 49층 대단지로 계획되었습니다. 2026년 상반기 조합 설립을 목표로 주민 동의가 빠르게 진행되고 있습니다.", redevelopment));
        
        postsToAdd.add(createPost("중구 신당10구역, 신통기획 1호로 2026년 착공 돌입", 
            "정비계획 수립 기간을 획기적으로 단축한 신당10구역이 2026년 내 착공을 앞두고 있습니다. 약 1,400세대의 도심형 명품 단지가 조성됩니다.", redevelopment));
        
        postsToAdd.add(createPost("서초구 방배13구역, 강남권 주택 공급의 핵심 축", 
            "방배동 일대 대규모 정비사업 중 하나인 방배13구역이 2026년 내 착공을 서두르며 강남권 신축 공급에 힘을 보탤 예정입니다.", redevelopment));
        
        postsToAdd.add(createPost("성동구 성수전략정비구역, 70층 초고층 시공사 선정", 
            "2026년부터 성수1구역을 필두로 본격적인 시공사 선정에 돌입합니다. 한강변 스카이라인을 바꿀 최대 유망지로 건설사들의 관심이 뜨겁습니다.", redevelopment));
        
        postsToAdd.add(createPost("동대문구 전농15구역, 청량리역 인근 신축 단지 조성", 
            "청량리역 역세권 배후 주거지인 전농15구역이 신속통합기획을 통해 대규모 아파트 단지로 재탄생하기 위한 정비계획 수립을 완료했습니다.", redevelopment));
        
        postsToAdd.add(createPost("광진구 광장동 극동아파트, 최고 49층 정비계획 결정", 
            "2026년 4월 정비구역 지정을 앞둔 광장동 극동아파트는 한강변 입지를 극대화하여 2,049세대의 대단지로 재건축될 계획입니다.", redevelopment));

        // --- 재건축 (Reconstruction) 10개 ---
        postsToAdd.add(createPost("잠실주공5단지, 2026년 대망의 착공 시작", 
            "서울 재건축의 가늠자인 잠실5단지가 2025년 시공사 선정 후 2026년 본격 착공합니다. 최고 70층 높이의 압도적 위용을 자랑할 예정입니다.", reconstruction));
        
        postsToAdd.add(createPost("반포주공1단지 1·2·4주구, 2026년 11월 일반분양", 
            "총 5,007가구 규모의 '디에이치 클래스트'가 드디어 일반분양에 나섭니다. 강남권 입성을 노리는 청약 대기자들의 치열한 경쟁이 예상됩니다.", reconstruction));
        
        postsToAdd.add(createPost("목동 6단지, 2026년 초 시공사 선정 '수주전 발발'", 
            "목동 신시가지 재건축의 첫 주자인 6단지가 시공사 선정 단계에 진입합니다. 대형 건설사들의 하이엔드 브랜드 경쟁이 본격화됩니다.", reconstruction));
        
        postsToAdd.add(createPost("여의도 대교아파트, 헤더윅 디자인의 혁신 건축물", 
            "여의도 대교아파트가 2026년 초 세계적인 건축가의 혁신 설계안을 확정하며, 단순한 아파트를 넘어선 예술적 랜드마크를 지향합니다.", reconstruction));
        
        postsToAdd.add(createPost("압구정 현대아파트, 2026년 정비계획 고시 마무리", 
            "압구정 특별계획구역 일대가 신속통합기획을 통해 2026년 행정 절차를 마무리하고 구체적인 재건축 청사진을 완성합니다.", reconstruction));
        
        postsToAdd.add(createPost("강북 백사마을, 2026년 본격 공사 '주거 보존형 재건축'", 
            "서울의 마지막 달동네 백사마을이 2026년 공사를 시작합니다. 기존 지형을 살린 주거 보존 방식과 신축 아파트가 공존하는 모델입니다.", reconstruction));
        
        postsToAdd.add(createPost("도곡 개포한신, 이주 완료 후 2026년 착공 돌입", 
            "관리처분인가 후 이주를 마친 개포한신이 2026년 착공에 들어갑니다. 최고 49층의 세련된 단지로 거듭날 준비를 마쳤습니다.", reconstruction));
        
        postsToAdd.add(createPost("서울시, '리모델링 대신 재건축' 규제 완화 효과 톡톡", 
            "공사비 상승과 규제 완화로 인해 많은 단지들이 리모델링에서 재건축으로 선회하고 있습니다. 2026년 정비사업 시장의 주요 변화입니다.", reconstruction));
        
        postsToAdd.add(createPost("여의도 시범아파트, 65층 초고층 재건축 행정지원 가속", 
            "신속통합기획 1호 사업지인 시범아파트가 2026년 사업시행인가를 목표로 순항하며 여의도 금융지구 배후 주거지의 위상을 높이고 있습니다.", reconstruction));
        
        postsToAdd.add(createPost("목동 1·2·3단지, 1만 가구 규모 정비계획 고시 완료", 
            "목동 1~3단지의 정비계획이 확정되어 목동 전체 재건축의 퍼즐이 완성되었습니다. 2026년은 단지별 조합 설립이 이어질 전망입니다.", reconstruction));

        postRepository.saveAll(postsToAdd);
        return "기존 데이터를 모두 삭제하고, '재개발' 10개 및 '재건축' 10개(총 20개)의 최신 소식을 성공적으로 등록했습니다.";
    }

    private Post createPost(String title, String content, Category category) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setCategory(category);
        post.setAuthor("전문 에디터");
        post.setCreatedAt(LocalDateTime.now());
        return post;
    }
}
