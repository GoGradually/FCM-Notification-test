import csv
import json
import random
import string

N = 60_0


ALPHABET = string.ascii_letters + string.digits

def random_token_152() -> str:
    return ''.join(random.choices(ALPHABET, k=152))

with open("dummy.csv", "w", newline="", encoding="utf-8") as f:
    w = csv.writer(f)
    w.writerow(["id","member_id","device_id", "token"])
    for i in range(N):
        w.writerow([i, i % 200, f"device_{i}", random_token_152()])
print("written:", N)


'''
1. csv 파일 생성
python3 dummy.py

2. 도커 컨테이너에 복사
docker cp ./dummy.csv mysql-db:/var/lib/mysql-files/dummy.csv

3. mysql에서 파일 매핑
LOAD DATA INFILE '/var/lib/mysql-files/dummy.csv' 
INTO TABLE push_subscription
character set utf8mb4 
FIELDS TERMINATED BY ',' ENCLOSED BY '"' 
LINES TERMINATED BY '\n' 
IGNORE 1 LINES (id, member_id, device_id, token);
'''