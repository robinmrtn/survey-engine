import React, {useRef} from "react";
import OpenTextQuestion from "./elements/OpenTextQuestion";
import ClosedQuestion from "./elements/ClosedQuestion";
import './SurveyPage.css'

export default function SurveyPage({page, inputs, setInputs}) {


    const submitButton = useRef(null)

    return (
        <div className="container">
            {page.surveyPageElements.map((element) => {

                switch (element.type) {
                    case 'opq':
                        return <div className="row">
                            <div className="col">
                                <OpenTextQuestion element={element} key={element.id}
                                                  inputs={inputs} setInputs={setInputs}/></div>
                        </div>
                    case 'clq':
                        return <div className="row">
                            <div className="col">
                                <ClosedQuestion element={element} key={element.id}
                                                inputs={inputs} setInputs={setInputs}/></div>
                        </div>
                    case 'opnq':
                        return <div className="row">
                            <div className="col">
                                <OpenTextQuestion element={element} key={element.id}
                                                  inputs={inputs} setInputs={setInputs}/></div>
                        </div>
                    }
                })}

        </div>
    );
}
