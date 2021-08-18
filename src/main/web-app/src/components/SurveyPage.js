import React, {useEffect, useRef} from "react";
import OpenTextQuestion from "./elements/OpenTextQuestion";
import ClosedQuestion from "./elements/ClosedQuestion";

export default function SurveyPage({page, pagePosition, setPagePosition, lastPage, inputs, setInputs}) {
    const isLastPage = pagePosition === lastPage;
    const isFirstPage = pagePosition === 0;
    console.log(page)

    const submitButton = useRef(null)

    useEffect(() => {

        if (submitButton.current !== null) {
            submitButton.current.blur()
        }

    }, []);


    function nextHandler(e) {
        e.preventDefault()
        if (!isLastPage) {
            setPagePosition(++pagePosition)
        }
    }

    function previousHandler(e) {
        e.preventDefault()
        if (!isFirstPage) {
            setPagePosition(--pagePosition)
        }
    }

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
                <div> {!isFirstPage &&
                <button style={{marginRight: "10px"}} className="btn btn-secondary"
                        onClick={previousHandler}>Previous</button>}
                    {!isLastPage
                        ? <button className="btn btn-primary" onClick={nextHandler}>Next</button>
                        : <button className="btn btn-success" ref={submitButton}>Submit</button>
                    }
                </div>
            </div>
        </div>
    );
}
