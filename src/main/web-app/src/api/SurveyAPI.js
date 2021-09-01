import axios from "axios";

const apiurl = process.env.REACT_APP_API_URL;

export async function getSurvey(id) {
    const url = apiurl + "surveys/" + id;
    const {data: response} = await axios.get(url);
    return response;
}

export async function getSurveys() {
    const url = apiurl + "surveys/"
    const {data: response} = await axios.get(url)
    return response
}

export async function postSurveyResponse(id, data) {
    const url = apiurl + "responses/campaigns/" + id;
    return await axios.post(url, data)
}