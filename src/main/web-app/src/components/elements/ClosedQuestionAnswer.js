import "./ClosedQuestionAnswer.css"

export default function ClosedQuestionAnswer({answer, inputs, setInputs, parentId}) {

    function onChangeHandler(e) {
        if (!(parentId in inputs)) {
            setInputs({...inputs, [parentId]: [answer.id]})
        } else {
            if (inputs[parentId].indexOf(answer.id) > -1) {
                const selectedAnswers = inputs[parentId];
                const answerIndex = selectedAnswers.indexOf(answer.id);
                console.log(answerIndex)
                selectedAnswers.splice(answerIndex, 1);
                setInputs({...inputs, [parentId]: selectedAnswers})
            } else {
                setInputs({...inputs, [parentId]: [...inputs[parentId], answer.id]})
            }
        }
    }

    function isChecked() {
        if (!(parentId in inputs)) {
            return false;
        }
        return inputs[parentId].indexOf(answer.id) > -1
    }

    return (
        <div className="form-check">
            <input className="form-check-input" type="checkbox" name={answer.id} onChange={onChangeHandler}
                   checked={isChecked()}/>
            <label className="form-check-label" htmlFor={answer.id}>{answer.value}</label>
        </div>
    )
}