import React from "react";
import SurveyPage from "./SurveyPage";

export default function Survey({survey}) {
    console.log(survey);
    return (
        <div>
            <h1>{survey.title}</h1>
            <p>{survey.description}</p>
            <SurveyPage></SurveyPage>
        </div>
    );
}
