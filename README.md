# FC온라인 전적검색
### 개발기간 : 2023년 09월 27일 ~ 현재 진행중

### [개요]

**FC온라인 전적검색 모바일 앱 개발**

Nexon 개발자센터에서 제공하는 Open Api를 사용한 모바일 전적검색 앱

이전에 개발한 [피파온라인4 전적검색 앱](https://github.com/KHJ-11/FIFA_Search/)이
FC온라인으로 변경되면서 해당 Open Api가 변경됨으로 새롭게 보완한 전적검색 앱
 
* 유저정보 : 기본정보를 제공 (닉네임, 레벨)
* 랭크정보 : 현재까지 달성한 유저의 최고등급 정보를 제공 (최고등급, 달성한날짜)
* 거래기록 : 유저가 최근에 구매 또는 판매한 선수의 정보를 제공 (거래한 선수명, 선수가치, 강화등급, 선수시즌)
* 팀스쿼드 : 유저가 최근에 사용한 스쿼드를 포메이션을 통해 직관적으로 나타냄 (팀 포메이션 및 선수)
* 경기기록 : 유저의 최근전적을 목록으로 나타냄 - 현재 작업중

### [라이브러리]
* Retrofit2
* Glide
* Jetpack Navigation
* ViewPager2

### [API]
* 넥슨 개발자센터 FC온라인 API

### [Preview]
![image](https://github.com/KHJ-11/FC_Online/assets/72050086/cc015bfc-54a1-4484-a917-a486ba6004d4)
![image](https://github.com/KHJ-11/FC_Online/assets/72050086/5a4be556-f7c6-4645-a378-f986ea7a6876)


시작화면 : 감독명 입력, 즐겨찾기된 감독명(클릭시 자동 입력)


![image](https://github.com/KHJ-11/FC_Online/assets/72050086/e95274f0-0161-4d0f-aa69-4d1e328cdff0)


홈 화면 : 감독정보(닉네임, 레벨), 달성한 최고 등급정보, 최근에 사용한 팀 스쿼드 및 선수정보, 즐겨찾기(별모양)


![image](https://github.com/KHJ-11/FC_Online/assets/72050086/36af249b-d7fa-4f51-b720-1a3730a30b41)
![image](https://github.com/KHJ-11/FC_Online/assets/72050086/1b87613b-f0ba-4825-8da9-927a89b52761)


거래목록 : 최근에 구매 및 판매한 선수(선수명, 선수가치, 시즌, 강화등급, 거래날짜)


![image](https://github.com/KHJ-11/FC_Online/assets/72050086/b4cb91fc-1a6d-45a9-ace2-e8e992299561)


경기기록 : 최근 전적 10경기 (승,무,패, 승률, 경기기록, 감독모드, 공식경기, 친선모드) - 현재 작업중
