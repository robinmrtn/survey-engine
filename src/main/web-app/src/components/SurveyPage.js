import React, {useRef} from "react";
import OpenTextQuestion from "./elements/OpenTextQuestion";
import ClosedQuestion from "./elements/ClosedQuestion";

export default function SurveyPage({page, inputs, setInputs}) {


    const submitButton = useRef(null)

    return (
        <div className='card'>
            <div className='card-body'>
                {page.surveyPageElements.map((element) => {

                    switch (element.type) {
                        case 'opq':
                            return <OpenTextQuestion element={element} key={element.id}
                                                     inputs={inputs} setInputs={setInputs}/>
                        case 'clq':
                            return <ClosedQuestion element={element} key={element.id}
                                                   inputs={inputs} setInputs={setInputs}/>
                        case 'opnq':
                            return <OpenTextQuestion element={element} key={element.id}
                                                     inputs={inputs} setInputs={setInputs}/>
                    }
                })}

            </div>
        </div>
    );
}
