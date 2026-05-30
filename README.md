<img width="823" height="649" alt="image" src="https://github.com/user-attachments/assets/5db33838-4edc-41da-8cec-a00c75a5c20f" />

# Feelio (필리오) - 당신의 마음을 읽어주는 AI 메이트 ☁️

> **편안하게 말하는 것만으로 기록되는 일상, 당신의 성향(F/T)에 맞춰 마음을 분석하고 위로를 건네는 AI 기반 감정 일기 서비스**

---

## 📌 서비스 개요
현대인들은 매일 수많은 감정을 소비하지만, 자신의 마음을 차분히 들여다보고 기록할 여유는 부족합니다. **Feelio**는  **음성(Voice)**으로 일기를 작성하고, AI가 사용자의 감정을 분석하고 맞춤형 페르소나로 피드백을 제공합니다. 더 나아가 익명의 공감 공간을 통해 따뜻한 연대감을 형성할 수 있도록 돕는 서비스입니다.

<br>

## 🌟 주요 기능 (Key Features)

### 1. 맞춤형 AI 메이트 페르소나 설정
* 사용자의 성향이나 그날 원하는 피드백 방식에 따라 AI 메이트의 성격을 선택할 수 있습니다.
* **다정한 공감 메이트 (F형):** 기분을 세심하게 살피고 따뜻한 위로와 든든한 응원의 메시지를 건네줍니다.
* **이성적인 분석 메이트 (T형):** 상황을 객관적으로 파악하고 마음을 차분하게 정리하여 이성적인 솔루션을 돕습니다.

### 2. 음성 기반 간편 일기 기록 (STT)
* 복잡하고 번거로운 텍스트 타이핑 대신, 마이크 버튼을 누르고 오늘 하루 있었던 일을 편안하게 이야기합니다.
* 자연어 처리 기술을 통해 대화하듯 편하게 말한 내용이 자동으로 기록되고 문맥이 파악됩니다.

### 3. 일일 감정 분석 및 익명 공감 커뮤니티
* 작성된 일기를 바탕으로 AI가 오늘의 핵심 감정(예: 행복, 슬픔, 분노 등)과 한 줄 평을 도출합니다.
* **감정 동질성 제공:** "오늘 나와 같은 감정을 느낀 사람"의 수를 시각적으로 보여주어 혼자가 아니라는 위로를 줍니다.
* **익명 공감글:** 같은 감정 상태의 유저들이 모여 "응원합니다!" 버튼 등으로 가볍고 따뜻하게 연대할 수 있는 소셜 요소를 제공합니다.

### 4. 월간 감정 통계 및 리포트
* 한 달 동안 누적된 감정 데이터를 시각화하여 대시보드로 제공합니다.
* **이번 달 메인 감정:** 한 달간 가장 많이 느낀 대표 감정과 이에 따른 상태 메시지(예: *'나 지금 광대 승천 중!'*)를 보여줍니다.
* **월간 통계 비교:** 지난달 대비 특정 감정(행복 등)의 수치 변화율을 제공합니다.
* **가장 행복했던 날 리마인드:** 월간 데이터 중 가장 긍정적인 수치가 높았던 날을 콕 집어 피드백해 줌으로써 긍정적인 감정을 되새기도록 합니다.

<br>

## 🛠 기술 스택 (Tech Stack)

### Frontend
- React
- Axios

### Backend
- Java 17 / Spring Boot
- Spring Data JPA
- Supabase (PostgreSQL with `pgvector`)

### AI
- Gemini API


<br>

## ⚙️ 시스템 아키텍처 (System Architecture)

                [ User / Client Browser ]
                           |
                           | Voice & Touch Input (HTTPS)
                           v
      --------------------- Vercel Network ---------------------
      |                                                        |
      |   [ React Application ]                                |
      |     - UI / UX Rendering                                |
      |     - Client State Management                          |
      |     - Audio Capturing                                  |
      |                                                        |
      ----------------------------------------------------------
                  |                              |
                  | Voice Audio / Blob           | REST API 
                  |                              | (JSON)
                  v                              v
        [ External STT API ]       ----------- Railway Network -----------
        (Web Speech API)           |                                     |
                                   |   [ Spring Boot Container ]         |
                                   |     (Active: Port 8080)             |
                                   |     - Business Logic & API          |
                                   |     - Emotion Diary Service         |
                                   |                                     |
                                   ---------------------------------------
                                          |                   |
                              JPA / SQL   |                   | Prompt & Text
                            Vector Search |                   |
                                          v                   v
                    --------------- Supabase ---------------  [ Google Gemini API ]
                    |                                      |  (F/T Persona Feedback
                    |   [ PostgreSQL ]                     |   & Emotion Extraction)
                    |     |-- Relational Data (Users)      |
                    |     |-- pgvector (Vector Embeddings) |
                    |                                      |
                    ----------------------------------------
