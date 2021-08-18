import {React, useEffect, useState} from "react";
import {getSurvey} from "../api/SurveyAPI";
import SurveyPage from "./SurveyPage";
import {useParams} from "react-router-dom";
import './SurveySite.css'

export default function SurveySite() {
    const [survey, setSurvey] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [pagePosition, setPagePosition] = useState(0)
    const {surveyId} = useParams();
    const [inputs, setInputs] = useState({})
    useEffect(() => {
        getSurvey(surveyId)
            .then((res) => setSurvey(res))
            .then(() => setLoading(false))
            .catch((err) => {
                setError(err);
            });
    }, []);

    if (survey !== null && error === null) {
        return (
            <div>
                <h1>{survey.title}</h1>
                <p>ID: {surveyId}</p>

                <p>{survey.description}</p>
                <SurveyPage page={survey.surveyPages[pagePosition]}
                            setPagePosition={setPagePosition}
                            pagePosition={pagePosition}
                            lastPage={survey.surveyPages.length - 1}
                            inputs={inputs}
                            setInputs={setInputs}/>
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
