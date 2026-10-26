import GamesGrid from "../../components/GamesGrid/GamesGrid"


export default function getGamesDestaque() {
    return (<>
    <GamesGrid page={1} pageSize={5} period="upcoming"/>
        <GamesGrid page={1} pageSize={30} period="recent"/>

    </>)

}