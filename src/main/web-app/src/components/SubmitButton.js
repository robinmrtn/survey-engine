import React from "react";

export default function SubmitButton({submitHandler, loading}) {


    return (
        <button className="btn btn-success"
                onClick={submitHandler}
                disabled={loading}

        >
            {loading && <span className="spinner-border spinner-border-sm" aria-hidden="true"/>}
            Submit
        </button>
    )
}