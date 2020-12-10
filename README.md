# StuffedBot
> 데큐플이 트롤링하려고 만든 두번째 봇

## Installation
1. git을 이용하여 클론해 줍니다. (`git clone https://github.com/playerdecuple/StuffedBot.git`)
2. 클론한 프로젝트를 InteliJ로 엽니다.
3. `TOKEN` 항목에 봇의 토큰을 넣습니다. (`OWNER` 항목은 ID를 넣으면 됩니다, 그런데 이거 수정 안해도 괜찮습니다.)
4. 빌드해 줍니다. 끝!

## Use
1. 자신의 서버에 초대합니다. 권한은 `관리자`여야 합니다.
2. 빌드하여 봇의 전원을 켭니다.
3. `!위장 [태그]` 형식으로 입력하여 위장시킵니다. (ex. `!위장 데큐플#9999`)
 * `!위장` 명령어의 `태그`에 OWNER의 ID를 갖고 있는 유저의 태그를 입력할 경우 자동으로 입력한 사람으로 위장합니다.
4. `!전송 [메시지]` 형식으로 메시지를 보냅니다.

### P.s.
디스코드 아바타 URL에서 ImageIO로 직접 얻어오려 하면 403(Forbidden) 에러를 뿜습니다..
그래서 HttpURLConnection의 setRequestProperty를 이용하고 ImageIO 소스를 좀 변형해서 쓰기로 했습니다. (`.setRequestProperty("User-Agent", "Mozilla")`)
와, 됩니다. 만세!
