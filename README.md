# 블로그 프로젝트

## 소개
이 프로젝트는 Java, CSS, HTML 및 JavaScript를 사용하여 만든 간단한 블로그 플랫폼입니다. Spring Boot 기반의 요구사항정의서 수행 미션형 프로젝트입니다.

## 기능 구현 현황
1. **회원가입**
   - [x] 회원 가입 폼
   - [x] 같은 ID, Email 중복 확인 API
   - [x] 회원 등록 기능
   - [x] 회원 가입 후 로그인 폼으로 이동

2. **로그인**
   - [x] 로그인 폼
   - [x] 로그인 기능
   - [x] 로그인 성공 후 루트 페이지로 이동
   - [x] 로그인 실패 시 오류 메시지와 함께 로그인 폼으로 이동
   - [x] Spring Security를 이용한 로그인 (Form Login, JWT Login, OAuth2 로그인)

3. **사이트 상단**
   - [ ] 좌측 상단 사이트 로고
   - [ ] 로그인 상태에 따른 링크 표시
     - 로그인 전: 로그인 링크
     - 로그인 후: 사용자 이름 및 설정, 블로그 이동 링크, 임시저장글 목록 보기, 로그아웃 링크

4. **로그아웃**
   - [x] 로그아웃 기능

5. **메인 페이지 (/)** 
   - [ ] 블로그 글 목록 보기 (최신 순, 조회수 높은 순, 즐겨찾기 순)
   - [ ] 페이징 처리 또는 무한 스크롤 구현
   - [ ] 제목, 내용, 사용자 이름으로 검색 기능

6. **블로그 글 쓰기**
   - [ ] 블로그 제목, 내용, 사진 등 입력
   - [ ] "출간하기"로 블로그 썸네일, 공개 유무, 시리즈 설정 후 등록
   - [ ] "임시저장" 기능

7. **임시글 저장 목록 보기**
   - [ ] 임시글 저장 목록 보기 링크 (로그인 후)
   - [ ] 임시글 수정 및 임시저장, 출간하기 기능

8. **특정 사용자 블로그 글 보기 (/@사용자아이디)**
   - [ ] 사용자 정보 보기
   - [ ] 사용자가 쓴 글 목록 보기 (최신 순, 조회수 높은 순, 즐겨찾기 순)
   - [ ] 페이징 처리 또는 무한 스크롤 구현
   - [ ] 사용자 태그 목록 보기 (태그당 글의 수 포함)
   - [ ] 제목, 내용으로 검색 기능

9. **시리즈 목록 보기**
   - [ ] 시리즈 목록 보기
   - [ ] 시리즈 제목 클릭 시 시리즈에 포함된 블로그 글 목록 보기

10. **블로그 글 상세 보기**
    - [ ] 제목 클릭 시 블로그 글 상세 보기

11. **사용자 정보 보기**
    - [ ] 사용자 정보 표시 (사용자 이름, 이메일)
    - [ ] 회원 탈퇴 링크 제공

12. **회원 탈퇴**
    - [ ] 회원 탈퇴 확인 폼
    - [ ] 회원 탈퇴 시 회원 정보 삭제

13. **댓글 기능**
    - [ ] 댓글 목록 보기 (최신 댓글부터)
    - [ ] 댓글 페이징 처리
    - [ ] 댓글 달기 및 대댓글 달기

14. **블로그 글 좋아요**
    - [ ] 블로그 글 좋아요 기능

## 사용된 기술
- **Java:** 백엔드 개발
- **Spring Boot:** 애플리케이션 프레임워크
- **CSS:** 프론트엔드 스타일링
- **HTML:** 웹 페이지 구조화
- **JavaScript:** 상호작용 추가
- **Spring Security:** 보안 및 인증
