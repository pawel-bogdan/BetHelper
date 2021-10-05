document.getElementById("submit").addEventListener("click", (event) => {
    startLoadingAnimation();
    event.preventDefault();
    document.getElementById("matches").innerHTML = "";
    const urlToBets = document.getElementsByTagName("input")[0].value;
    fetch("http://localhost:8080/matches/" + urlToBets).then(resp => {
        if(resp.status == 400) {
            informAboutIncorrectUrl();
        }
        else {
            resp.json().then(matches => {
                matches.forEach(createMatch);
            });
        }
        stopLoadingAnimation();
    });
});

function startLoadingAnimation() {
    document.getElementById("loader").style.display = "block";
}

function stopLoadingAnimation() {
    document.getElementById("loader").style.display = "none";
}
function informAboutIncorrectUrl() {
    const incorrectUrlDiv = document.createElement("div");
    incorrectUrlDiv.id = "incorrectUrlDiv";

    const infoMsg = document.createElement("p");
    infoMsg.id = "infoMsg";
    infoMsg.textContent = "URL address must starts with: https://www.efortuna.pl/zaklady-bukmacherskie/pilka-nozna/"
    incorrectUrlDiv.appendChild(infoMsg);
    document.getElementById("matches").appendChild(incorrectUrlDiv);
}

function createMatch(match) {
    // Main container containing match info and odds(courses).
    const matchDiv = document.createElement("div");
    matchDiv.classList.add("match");
    matchDiv.appendChild(createMatchInfoDiv(match));
    matchDiv.appendChild(createOddsDiv(match));
    document.getElementById("matches").appendChild(matchDiv);
}

function createMatchInfoDiv(match) {
    const hostClub = match.hostClub;
    const guestClub = match.guestClub;

    const matchInfoDiv = document.createElement("div");

    const hostClubDiv = document.createElement("div");
    hostClubDiv.classList.add("club");
    const guestClubDiv = document.createElement("div");
    guestClubDiv.classList.add("club");

    const hostClubNameSpan = document.createElement("span")
    hostClubNameSpan.classList.add("clubName");
    hostClubNameSpan.textContent = hostClub.shortName;
    const guestClubNameSpan = document.createElement("span")
    guestClubNameSpan.classList.add("clubName");
    guestClubNameSpan.textContent = guestClub.shortName;

    const hostClubSquadValueSpan = document.createElement("span");
    const guestClubSquadValueSpan = document.createElement("span");

    hostClubSquadValueSpan.textContent = "SV: " + hostClub.squadValue;
    guestClubSquadValueSpan.textContent = "SV: " + guestClub.squadValue;
    hostClubSquadValueSpan.classList.add("additionalData");
    guestClubSquadValueSpan.classList.add("additionalData");

    const hostClubSVCRatioSpan = document.createElement("span");
    const guestClubSVCRatioSpan = document.createElement("span");

    hostClubSVCRatioSpan.textContent = "SVC RATIO: " + match.hostTeamSVCRating;
    guestClubSVCRatioSpan.textContent = "SVC RATIO: " + match.guestTeamSVCRating;
    hostClubSVCRatioSpan.classList.add("additionalData");
    guestClubSVCRatioSpan.classList.add("additionalData");

    const dateDiv = document.createElement('div');
    const dateSpan = document.createElement('span');

    dateSpan.textContent = match.date;
    dateDiv.appendChild(dateSpan);

    hostClubDiv.appendChild(hostClubNameSpan);
    hostClubDiv.appendChild(hostClubSquadValueSpan);
    hostClubDiv.appendChild(hostClubSVCRatioSpan)

    guestClubDiv.appendChild(guestClubNameSpan);
    guestClubDiv.appendChild(guestClubSquadValueSpan);
    guestClubDiv.appendChild(guestClubSVCRatioSpan)

    matchInfoDiv.appendChild(hostClubDiv);
    matchInfoDiv.appendChild(dateDiv);
    matchInfoDiv.appendChild(guestClubDiv);

    matchInfoDiv.classList.add("matchInfo");
    return matchInfoDiv;
}

function createOddsDiv(match) {
    const oddsDiv = document.createElement("div");
    const hostTeamVictoryOddsSpan = document.createElement("span");
    const drawOddsSpan = document.createElement("span");
    const guestTeamVictoryOddsSpan = document.createElement("span");
    const hostTeamVictoryOrDrawOddsSpan = document.createElement("span");
    const guestTeamVictoryOrDrawOddsSpan = document.createElement("span");

    hostTeamVictoryOddsSpan.textContent = "[1] " +  match.hostTeamVictoryCourse;
    drawOddsSpan.textContent = "[0] " +  match.drawCourse;
    guestTeamVictoryOddsSpan.textContent = "[2] " +  match.guestTeamVictoryCourse;
    hostTeamVictoryOrDrawOddsSpan.textContent = "[10] " +  match.hostTeamVictoryOrDrawCourse;
    guestTeamVictoryOrDrawOddsSpan.textContent = "[02] " +  match.guestTeamVictoryOrDrawCourse;


    oddsDiv.classList.add("odds");
    oddsDiv.appendChild(hostTeamVictoryOddsSpan);
    oddsDiv.appendChild(drawOddsSpan);
    oddsDiv.appendChild(guestTeamVictoryOddsSpan);
    oddsDiv.appendChild(hostTeamVictoryOrDrawOddsSpan);
    oddsDiv.appendChild(guestTeamVictoryOrDrawOddsSpan);

    return oddsDiv;
}

function processOkResponse(response = {}) {
    if (response.ok) {
        return response.json();
    }
    throw new Error(`Status not 200 (${response.status})`);
}