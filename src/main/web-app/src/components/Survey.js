import React, {useEffect, useRef, useState} from "react";
import SurveyPage from "./SurveyPage";
import {useParams} from "react-router-dom";
import {getSurvey, postSurveyResponse} from "../api/SurveyAPI";
import ErrorDialog from "./ErrorDialog";
import LoadingSpinner from "./LoadingSpinner";

export default function Survey() {
    const [survey, setSurvey] = useState(null);

    const [error, setError] = useState(null);
    const [pagePosition, setPagePosition] = useState(0)
    const {surveyId} = useParams();
    const [inputs, setInputs] = useState({})
    useEffect(() => {
        getSurvey(surveyId)
            .then((res) => setSurvey(res))
            .catch((err) => {
                setError(err);
            });
    }, []);


    const isFirstPage = pagePosition === 0;
    let isLastPage = -1;

    const submitButton = useRef(null)

    function nextHandler(e) {
        e.preventDefault()
        if (!isLastPage) {
            setPagePosition(pagePosition + 1)
        }
    }

    function previousHandler(e) {
        e.preventDefault()
        if (!isFirstPage) {
            setPagePosition(pagePosition - 1)
        }
    }

    function submitHandler(e) {
        e.preventDefault()
        let dto = inputsToResponseDto(inputs);
        postSurveyResponse(surveyId, dto).then(r => console.log(r))
    }

    function inputsToResponseDto(inputs) {
        let dto = []
        for (let [key, value] of Object.entries(inputs)) {
            key = parseInt(key)
            dto.push({id: key, value: value, type: getElementTypeById(key)})
        }
        return {elementResponseDtos: dto}
    }

    function getElementTypeById(id) {
        const element = survey.surveyPages.reduce((acc, x) => acc.concat([x.surveyPageElements]), [])
            .reduce((acc, x) => [...acc, ...x], [])
            .find(e => e.id === id);
        return element.type
    }

    if (survey !== null && error === null) {
        isLastPage = pagePosition === survey.surveyPages.length - 1
        return (
            <div>
                <h1>{survey.title}</h1>
                <p>{survey.description}</p>
                <SurveyPage
                    page={survey.surveyPages[pagePosition]}
                    setPagePosition={setPagePosition}
                    pagePosition={pagePosition}
                    lastPage={survey.surveyPages.length - 1}
                    inputs={inputs}
                    setInputs={setInputs}/>
                <div style={{"margin": "20px"}}>
                    <div> {!isFirstPage &&
                    <button style={{marginRight: "10px"}} className="btn btn-primary"
                            onClick={previousHandler}>Previous</button>}
                        {!isLastPage
                            ? <button className="btn btn-primary" onClick={nextHandler}>Next</button>
                            : <button className="btn btn-success" ref={submitButton}
                                      onClick={submitHandler}>Submit</button>
                        }
                    </div>
                </div>
            </div>
        );
    } else if (error !== null) {
        return <ErrorDialog message={error.message}/>
    } else {
        return <LoadingSpinner/>
    }
}
