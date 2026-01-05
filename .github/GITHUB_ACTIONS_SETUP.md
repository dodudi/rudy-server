# GitHub Actions 설정 가이드

이 프로젝트는 GitHub Actions를 사용하여 자동으로 Docker 이미지를 빌드하고 Docker Hub에 푸시합니다.

## 사전 준비

### 1. Docker Hub Access Token 생성

1. [Docker Hub](https://hub.docker.com/)에 로그인
2. Account Settings → Security → New Access Token
3. Token Description: `github-actions-rudy-devtools`
4. Access permissions: `Read, Write, Delete` 선택
5. Generate 버튼 클릭
6. **생성된 토큰을 복사** (다시 볼 수 없습니다!)

### 2. GitHub Secrets 설정

1. GitHub Repository 페이지로 이동
2. **Settings** → **Secrets and variables** → **Actions**
3. **New repository secret** 클릭
4. 다음 두 개의 secret을 추가:

#### Secret 1: DOCKERHUB_USERNAME
- Name: `DOCKERHUB_USERNAME`
- Secret: `didadu` (Docker Hub 사용자명)

#### Secret 2: DOCKERHUB_TOKEN
- Name: `DOCKERHUB_TOKEN`
- Secret: (1단계에서 복사한 Access Token)

## 워크플로우 동작 방식

### 트리거 조건
- `main` 또는 `master` 브랜치에 push할 때 자동 실행
- 수동 실행: GitHub Actions 탭에서 "Run workflow" 버튼 클릭

### 빌드 프로세스
1. ✅ 코드 체크아웃
2. ✅ JDK 21 설정
3. ✅ Gradle 빌드 (테스트 제외)
4. ✅ Docker Buildx 설정
5. ✅ Docker Hub 로그인
6. ✅ 멀티 아키텍처 이미지 빌드 (AMD64, ARM64)
7. ✅ Docker Hub에 푸시

### 생성되는 이미지
- **Repository**: `didadu/rudy-devtools`
- **Tags**:
  - `latest` - 최신 빌드
  - `main-<git-sha>` - Git commit SHA 태그

## 사용 방법

### 자동 빌드
```bash
git add .
git commit -m "Update code"
git push origin main
```

Push 후 GitHub Actions 탭에서 빌드 진행 상황을 확인할 수 있습니다.

### 수동 빌드
1. GitHub Repository → Actions 탭
2. "Build and Push Docker Image" 워크플로우 선택
3. "Run workflow" 버튼 클릭

## 빌드 확인

### GitHub Actions 로그 확인
1. Repository → Actions 탭
2. 최근 워크플로우 실행 클릭
3. 각 단계별 로그 확인

### Docker Hub 확인
1. [Docker Hub](https://hub.docker.com/)
2. Repositories → didadu/rudy-devtools
3. Tags 탭에서 새로 푸시된 이미지 확인

## 문제 해결

### Build Failed
- Gradle 빌드 실패: 로컬에서 `./gradlew build` 테스트
- Docker 빌드 실패: Dockerfile 문법 확인

### Authentication Failed
- Docker Hub credentials 확인
- GitHub Secrets 재설정

### Permission Denied
- gradlew 실행 권한: 워크플로우에서 자동 설정됨
- Docker Hub Access Token 권한 확인

## 비용

- **GitHub Actions**: Public repository는 무료 (월 2,000분 제공)
- **Docker Hub**: 무료 플랜 (1개 private repo, 무제한 public repo)

## 보안

- ⚠️ **절대 Access Token을 코드에 커밋하지 마세요**
- ✅ GitHub Secrets를 통해서만 인증 정보 관리
- ✅ Access Token은 필요한 권한만 부여

## 추가 설정

### 다른 브랜치에서도 빌드하려면
`.github/workflows/docker-build.yml` 파일의 `branches` 섹션 수정:
```yaml
on:
  push:
    branches:
      - main
      - develop  # 추가
      - feature/* # 패턴 매칭
```

### 테스트 포함하려면
`./gradlew build -x test` → `./gradlew build`로 변경
