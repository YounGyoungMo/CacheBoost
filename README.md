![header](https://capsule-render.vercel.app/api?type=waving&color=auto&height=250&section=header&text=CacheBoost%20Project&fontSize=80)

## 개요

CacheBoost는 도서 검색 기능과 인기 검색어 랭킹 조회 기능을 제공하는 백엔드 프로젝트입니다.

- JWT 기반 로그인

- 도서 검색 결과 캐싱

- 인기 키워드 집계 및 랭킹 조회

위 기능들을 통해 검색 성능 최적화와 캐시 활용에 중점을 두어 개발하였습니다.

실제 상용 서비스인 Yes24를 벤치마킹하여,
복잡한 기능보다는 핵심 기능을 간결하게 구현하는 데 집중하였습니다.


## 개발 환경
언어 : ![Static Badge](https://img.shields.io/badge/Java-red?style=flat-square)

JDK : ![Static Badge](https://img.shields.io/badge/JDK-17-yellow?style=flat-square)

프레임워크 : ![Static Badge](https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=Spring&logoColor=white)

RDB : ![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)

REDIS : ![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)

ORM : ![Static Badge](https://img.shields.io/badge/JPA-FFA500?style=flat)

## 🔠 목차

1. [API 명세서](#-api-명세서)
2. [ERD](#-erd)
3. [기능 요약](#-기능-요약)
4. [디렉토리 구조](#-디렉토리-구조)

# API 명세서

### 1. 도서 api 명세서
![도서 api명세서](https://github.com/user-attachments/assets/18d78350-82ad-4926-b71a-4950ed4cd74f)

### 2. 유저 api 명세서
![유저 api명세서](https://github.com/user-attachments/assets/7a70a295-ff2d-4c15-b3a9-cfebae11dc75)

### 3. 로그인 api 명세서
![로그인 api 명세서](https://github.com/user-attachments/assets/39033f11-039a-4531-8de7-bdfdcaa48871)

### 4. 배송주소 api 명세서
![배송주소 api명세서](https://github.com/user-attachments/assets/948138f5-6dea-47ee-b060-0f6d3379e891)

### 5. 인기검색어 api 명세서
![인기 검색어 api명세서](https://github.com/user-attachments/assets/5188aad3-ef2f-4f1f-a7a0-7e4ee0b3c1bd)

### 6. 검색기록 api 명세서
![검색기록 api명세서](https://github.com/user-attachments/assets/14137b4c-5870-4fde-a471-2044e2d6f7a0)

# ERD
![CacheBoost 공개용ERD](https://github.com/user-attachments/assets/c6a564ec-bcdc-4a15-951c-b330a7570911)

# 기능 요약

- 도서 검색

  -  도서명 기준 검색 (LIKE 조건)

  - 검색 결과 페이지네이션 처리

  - @Cacheable 기반 Redis 캐시 적용

- 인기 검색어 랭킹

    - Redis를 활용한 키워드 카운팅

    - 검색 시 Redis에 조회 키워드 저장

    - 인기 검색어 조회 API 제공

- 검색 기록 저장

  - 사용자의 개인 검색 이력 저장


- JWT 기반 인증/인가

    - 로그인 시 JWT Access & Refresh 토큰 발급

    - Access Token 블랙리스트 처리로 로그아웃 구현

- Spring Security 설정

    - 권한 기반 접근 제어



# 디렉토리 구조
```angular2html
com.example.CacheBoost
├── common              # 공통 모듈 (AOP, Config, Exception, Security 등)
│   ├── aop
│   ├── config
│   ├── exception
│   └── security
├── domain              # 도메인 중심 패키지 구성
│   ├── address
│   ├── auth
│   ├── book
│   ├── searchhistory
│   ├── searchkeyword
│   └── user
└── CacheBoostApplication.java

```