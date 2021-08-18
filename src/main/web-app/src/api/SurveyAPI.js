import axios from "axios";

const apiurl = process.env.REACT_APP_API_URL;

export async function getSurvey(id) {
    const url = apiurl + "surveys/" + id;
    const {data: response} = await axios.get(url);
    return response;
}