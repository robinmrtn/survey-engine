import {React, useEffect, useState} from "react";
import {getSurveys} from "../api/SurveyAPI";
import ErrorDialog from "./ErrorDialog";
import LoadingSpinner from "./LoadingSpinner";
import {Link} from "react-router-dom";


export default function SurveyList() {

    const [surveys, setSurveys] = useState({});

    const [error, setError] = useState(null);
    useEffect(() => {
        getSurveys()
            .then(res => {
                setSurveys(res);
                console.log(res)
            })
            .catch(err => setError(err))

    }, []);

    if (surveys && Object.keys(surveys).length !== 0) {
        return<ul>{surveys.map(element => <li key={element.id}><Link to={'/survey/' + element.id}>{element.name}</Link>
        </li>)} </ul>
    } else if (error !== null) {
        return (
            <ErrorDialog message={error.message}/>
        );
    } else {
        return <LoadingSpinner/>
    }
}