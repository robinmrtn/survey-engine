import {React, useEffect, useState} from "react";
import {getSurvey} from "../api/SurveyAPI";
import SurveyPage from "./SurveyPage";
import {useParams} from "react-router-dom";
import './SurveySite.css'

export default function SurveySite() {
    const [survey, setSurvey] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const {surveyId} = useParams();
    useEffect(() => {
        getSurvey(surveyId)
            .then((res) => setSurvey(res))
            .then(() => setLoading(false))
            .catch((err) => {
                setError(err);
            });
    }, []);

    if (survey !== null && error === null) {
        console.log(survey)
        return (
            <div>
                <h1>{survey.title}</h1>
                <p>ID: {surveyId}</p>
                <p>{survey.description}</p>
                <SurveyPage/>
            </div>
        );
    } else if (error !== null) {
        return (
            <div className='alert alert-danger card' role='alert'>
                <span>{error.message}</span>
            </div>
        );
    } else {
        return <div className="spinner-border text-secondary" role="status">
        </div>
    }
}
