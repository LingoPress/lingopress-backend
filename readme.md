# lingopress-backend

`링고프레스` 프로젝트의 백엔드 저장소입니다.

### 홈페이지 링크

https://lingopress.me

### 프로젝트 소개

**링고프레스!** 당신의 영어 학습을 위한 새로운 파트너! 📘✨

링고프레스는 번역을 중심으로 영자뉴스를 통해 영어 학습을 지원하는 웹 서비스입니다. 원하는 영자신문을 선택하고, 기사를 번역하며 모르는 단어를 클릭하면 바로 그 뜻을 확인할 수
있습니다. 🗞️🔍

독해, 어휘, 시사까지, 기사를 읽으며 다양한 영역의 영어를 한 번에 학습하세요. 발견한 새로운 단어들은 개인 단어장에 저장되어, 언제든지 복습할 수 있습니다. 📚💼

읽었던 기사들과 번역에 도전했던 내용들은 별도로 모아, 학습의 효과를 극대화할 수 있도록 도와드립니다. 📋🎓

### 시연 영상

추가예정

### 개발 환경 및 도구

언어: Java

프레임워크: Spring Boot

라이브러리: JPA, Lombok, Spring Security, Spring Data JPA, Spring Web, Feign Client

데이터베이스: postgreSQL

클라우드: AWS EC2

인프라: Docker

### 인프라 구조

- ec2 하나에 여러 Docker 컨테이너로 구성되어 있습니다.
- 각 컨테이너는 lingopress_network라는 도커 네트워크를 통해 서로 통신하며 frontend 컨테이너만 외부에서 접근할 수 있도록 구성하였습니다.
- `lingopress-nginx` 컨테이너는 Nginx를 통해 동작하며 프론트 화면을 정적으로 제공합니다. 또한, backend 컨테이너로의 요청을 받아 리버스 프록시로
  처리합니다. 유일하게 외부와 통신하는 컨테이너입니다.
- `lingopress-backend` 컨테이너는 Spring Boot로 구성되어 있으며, postgreSQL과 연결되어 있습니다.
- `lingopress-db` 컨테이너는 postgreSQL을 사용하며, 데이터베이스를 저장합니다.
- `lingopress-scarper` 컨테이너는 python을 사용하여 영자신문을 스크래핑하고 deepL로 번역하여 데이터베이스에 저장합니다. ec2가 크론을 통해 하루에
  한번씩만 실행합니다.
- `lingopress-ai` 컨테이너는 chatgpt api를 사용하여 번역을 처리합니다. 추후 문장 유사도 분석기능을 추가하면서 온프레미스로 변경할 예정입니다.
- docker 컨테이너를 관리하기 위해 `portainer`를 사용하였습니다.
- docker 이미지는 docker hub에 public으로 업로드 되기에, 스프링과 도커의 환경변수 기능을 이용해 입력하도록 구성하였습니다. 