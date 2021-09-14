import ClosedQuestionAnswer from "./ClosedQuestionAnswer";

export default function ClosedQuestion({element, inputs, setInputs}) {
    return (
        <div className="card">
            <div className="card-body">
                <h5 className="card-title">
                    {element.question}
                </h5>
                <div>
                    {element.answers.map((answer) => {
                        return (<ClosedQuestionAnswer answer={answer} key={answer.id}
                                                      inputs={inputs} setInputs={setInputs}
                                                      parentId={element.id}/>
                        )
                    })}
                </div>
            </div>
        </div>
    )
}