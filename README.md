# 팀 프로젝트 이츠마인 개선 및 보완
## Sliding Expiration(슬라이딩 만료) 방식 적용
* 엑세스 토큰 유효시간이 1/4 이하로 남았을 때 인가 단계에서 엑세스 토큰을 확인 할 때 유효시간을 확인하고 새로운 엑세스 토큰으로 교체 
## JDBC를 이용한 1M건 벌크 데이터 insert
* 10000건 씩 batch insert로 1M건 벌크 데이터 insert
## mysql 프로시저를 이용한 1M건 벌크 데이터 insert
* 10000건 씩 batch insert로 1M건 벌크 데이터 insert
* 1M건 벌크 데이터 insert(대량의 데이터를 한꺼번에 넣으면 트렉젝션이 너무 무거워져 오류 위험)
