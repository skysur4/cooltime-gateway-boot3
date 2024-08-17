![Springboot](https://shields.io/badge/v3.2.4-6DB33F?logo=springboot&label=Spring%20Boot&style=social)  
![Springcloud](https://shields.io/badge/2023.0.1-6DB33F?logo=springboot&label=Spring%20Cloud&style=social)  
![Gradle](https://img.shields.io/badge/v8.7-02303A?logo=gradle&label=Gradle&style=social)  
![Postgresql](https://img.shields.io/badge/v13-4169E1?logo=postgresql&label=PostgreSQL&style=social)  

# 쿨타임 게이트웨이 프로젝트

## 프로젝트 설정 가이드
![Static Badge](https://img.shields.io/badge/warning-orange) Keycloak IdP를 기준 설정이며 ```issuer-uri``` URL 설정 필수

#### IDE 설정
###### Spring Tool Suite (STS4)
- `Explorer`의 프로젝트를 오른클릭 후 `Configure > Add Gradle Nature` 클릭

- 빌드가 완료되면 Boot Dashboard에 프로젝트가 보임

- Boot Dashboard의 프로젝트를 오른클릭 후 `Open Config` 클릭

- `Environment` 탭을 클릭 후 `Add` 버튼 클릭

- 필드값을 아래와 같이 입력

**Name**			|**Value**
:------------------	|:--------
ENCRYPTOR_PROFILE	|<패스워드>

- `OK` 버튼 `Apply` 버튼 `Close` 버튼을 차례로 클릭

- Boot Dashboard의 프로젝트를 `start` 하여 서버가 정상적으로 로딩되는지 확인

###### IntelliJ
- 상단 오른쪽의 `▶` 버튼을 클릭하여 어플리케이션을 실행

- 상단 오른쪽의 `: -> Edit Configuration` 메뉴를 클릭

- `Build and run` 메뉴에 `Environment variables` 입력칸이 없다면 아래와 같이 생성
	- `Build and run` 메뉴의 `Moidfy options` 클릭

	- `Environment variables` 클릭

- Environment variables 필드값 아래와 같이 입력

**Name**			|**Value**
:------------------	|:--------
ENCRYPTOR_PROFILE	|<패스워드>

- `OK` 버튼 `Apply` 버튼 `Close` 버튼을 차례로 클릭

- 상단 오른쪽의 `▶` 버튼을 다시 클릭하여 어플리케이션이 정상적으로 로딩되는지 확인

#### Redis 설치
Github [Redis](https://github.com/microsoftarchive/redis)에서 설치파일을 다운로드 후 설치  
로컬 PC에 설치하며 추가적인 설정은 하지 않음

## 프로젝트 개발 가이드
###### 2. 인증
- 인증 처리 방법은 authorization 디렉토리 내 파일을 참고
	- CooltimeAuthenticationFailureHandler.java
	- CooltimeAuthenticationSuccessHandler.java
	- CooltimeLogoutSuccessHandler.java
	- CooltimeOAuth2AuthorizationRequestResolver.java

- ID Provider와의 연계는 환경별 application*.yaml의 스프링 항목 참고

```yaml
###########################################################################
### Spring 설정 
###########################################################################
spring:
  ### 보안 설정 ###
  security:
    ### OIDC 연동 설정 ###
    oauth2:
      client:
        provider: #oidc 스펙 지원 시 사용
          kakao:
            authorization-uri: https://kauth.kakao.com/oauth/authorize
            token-uri: https://kauth.kakao.com/oauth/token
            issuer-uri: https://kauth.kakao.com
            jwk-set-uri: https://kauth.kakao.com/.well-known/jwks.json
            user-info-uri: https://kapi.kakao.com/v1/oidc/userinfo
            user-name-attribute: name
        registration:
          kakao:
            client-name: "Kakao Oauth2 Client"
            client-id: asdf234 #ENC(Yg6dVjID6lwGAHaAd57fNX30Oe8BWTXulirZ/Or629P63QZ7vH4v0nhPI7iHfIrU)
            client-secret: asdf234 #ENC(XZhlr3OttWpb+hXyj8xGoZTY2AICrW/6aUtt2Le438TJehaGgOmEHSM20HR0wDoO)
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/${spring.application.root}/login/oauth2/code/{registrationId}"
            scope: "oidc"
      resourceserver:
        jwt:
          jwk-set-uri: https://kauth.kakao.com/.well-known/jwks.json
          issuer-uri: https://kauth.kakao.com
```

###### 2. 라우팅
- 기본 로직은 RouterConfig.java를 참고

- 환경별 application*.yaml에서 설정하며 프로퍼티별 사용 방법은 아래와 같음

```yaml
###########################################################################
### Router data 설정 
###########################################################################
routers:
  defaults:
    description:
      context: description # 라우팅 소스 컨텍스트
      scheme: http # 라우팅 대상 스키마
      host: localhost # 라우팅 대상 주소 (ip/domain)
      port: 8080 # 라우팅 대상 포트
      root: false # 컨텍스트 제거, true일 경우 prefix와 rewrite 무시 
      prefix: test # 라우팅 대상 path의 맨 앞에 추가되는 path
      rewrite: explain/v1 # 라우팅 대상 컨텍스트
      skip-filters: true # 라우팅 필터 적용 유무, true일 경우 모든 GatewayFilter를 우회
```

###### 3. 보안 설정
- 기본 로직은 SecurityConfig.java를 참고

- swagger, login 페이지 등의 보안 해제 url은 application*.yml의 스프링 항목을 참고

```yaml
###########################################################################
### Spring 설정 
###########################################################################
spring:
    access:
      excludes:
      - /login**
      - /logout**
```

###### 4. Redis 세션 공유
- 기본 로직은 SessionConfig.java를 참고

- 대부분 기본값을 유지하고 있으며, 세션 타임아웃 시간만 테스트 목적을 위해 따로 설정 함

###### 5. 스웨거
- 기본 로직은 SwaggerConfig.java를 참고

- 향후 추가될 서비스에 따라 수정되어야 하며, 보안상 운영 프로파일 사용 시 노출을 금지하도록 설정됨

- 기본 설정은 application.yml의 springdoc 항목 참고

```yaml
###########################################################################
### Swagger 설정 
###########################################################################
springdoc:
  packages-to-scan:
  - io.cooltime.gateway.controller
  show-actuator: true
  swagger-ui:
    use-root-path: true
    path: /${spring.application.root}/swagger.html
```

###### 6. cors
- 기본 로직은 WebfluxConfig.java를 참고

- corsWebFilter 빈을 통해 CORS 등록을 완료

- 등록 대상은 환경별 application*.yml 파일의 Application 설정 항목을 참고

```yaml
###########################################################################
### Application 설정 
###########################################################################
  cors: 
    allowed-origin:
    - http://cooltime.io
```

###### 7. exception
- 기본 로직은 ExceptionConfig.java를 참고

- GlobalErrorWebExceptionHandler를 사용하여 기본적인 구성만 되어 있음

## Gradle 빌드 및 실행
#### 빌드
```shell
$ ./gradlew clean build -x test
```

#### 실행
```shell
$ java -jar -Dspring.profiles.active=local -DENCRYPTOR_PROFILE=<패스워드> build/libs/cooltime-gateway-0.0.999.jar
```
