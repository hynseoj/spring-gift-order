package gift.domain.order.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.AuthProvider;
import gift.auth.jwt.JwtProvider;
import gift.domain.order.service.OrderService;
import gift.domain.product.entity.Category;
import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import gift.domain.user.entity.Role;
import gift.domain.user.entity.User;
import gift.domain.user.repository.UserJpaRepository;
import io.jsonwebtoken.Claims;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserJpaRepository userJpaRepository;

    @MockBean
    private JwtProvider jwtProvider;

    @Autowired
    private ObjectMapper objectMapper;

    private static final User user = new User(1L, "testUser", "test@test.com", "test123", Role.USER, AuthProvider.LOCAL);
    private static final Category category = new Category(1L, "교환권", "#FFFFFF", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "test");
    private static final Product product = new Product(1L, category, "아이스 카페 아메리카노 T", 4500, "https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[110563]_20210426095937947.jpg");
    private static final Option option = new Option(1L, product, "사과맛", 90);


    @BeforeEach
    void setUp() {
        given(userJpaRepository.findById(any(Long.class))).willReturn(Optional.of(user));

        Claims claims = Mockito.mock(Claims.class);
        given(jwtProvider.getAuthentication(any(String.class))).willReturn(claims);
        given(claims.getSubject()).willReturn(String.valueOf(user.getId()));
    }

//    @Test
//    @DisplayName("주문 생성 테스트")
//    void create() throws Exception {
//        // given
//        product.addOption(option);
//        OrderRequest orderRequest = new OrderRequest(product.getId(), option.getId(), 10, "테스트 와하하");
//        OrderResponse orderResponse = OrderResponse.from(orderRequest.toOrder(user, product, option));
//        String jsonContent = objectMapper.writeValueAsString(orderRequest);
//
//        given(orderService.create(any(OrderRequest.class), any(User.class))).willReturn(orderResponse);
//
//        // when & then
//        mockMvc.perform(post("/api/orders")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(jsonContent)
//            .header("Authorization", "Bearer token"))
//            .andExpect(status().isCreated())
//            .andExpect(content().json(objectMapper.writeValueAsString(orderResponse)));
//    }
}