import ClosedQuestionAnswer from "./ClosedQuestionAnswer";

export default function ClosedQuestion({element, inputs, setInputs}) {
    return (
        <div>
            <p><b>
                {element.question}
            </b>
            </p>
            <div>
                {element.answers.map((answer) => {
                    return (<ClosedQuestionAnswer answer={answer} key={answer.id}
                                                  inputs={inputs} setInputs={setInputs}
                                                  parentId={element.id}/>
                    )
                })}
            </div>
        </div>
    )
}