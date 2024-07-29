# spring-gift-order

## 1단계 - 카카오 로그인
> 카카오 API를 사용하기 위해 애플리케이션을 등록한다.   
> 카카오 로그인을 통해 인가 코드를 받고, 인가 코드를 사용해 토큰을 받는다.

### 기능 요구사항 목록
- [x] 인가 코드 받기
- [x] 액세스 토큰 추출
- [x] 사용자 로그인 및 회원 가입
- [x] 오류 처리


## 2단계 - 주문하기
> 카카오톡 메시지 API를 사용하여 주문하기 기능을 구현한다.   
> 주문할 때 수령인에게 보낼 메시지를 작성할 수 있다.   
> 상품 옵션과 해당 수량을 선택하여 주문하면, 해당 상품 옵션의 수량이 차감된다.   
> 해당 상품이 주문자의 위시리스트에 있는 경우, 위시리스트에서 삭제한다.   
> 주문 내역을 카카오톡 메시지로 전송한다.

### 기능 요구사항 목록
- [x] 주문 엔티티 클래스 작성
- [x] 주문 기능 구현
- [x] 카카오톡 메시지 API 클라이언트 구현
- [x] 주문 내역 전송 기능 구현


## 3단계 - API 문서 만들기
> API 사양에 관해 클라이언트와 어떻게 소통할 수 있을지 고민해 보고 그 방법을 구현한다.

### 기능 요구사항 목록
- [x] API 문서 만들기