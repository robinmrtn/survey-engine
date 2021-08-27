import {React} from "react";

export default function ErrorDialog({message}) {
    return (
        <div className='alert alert-danger card' role='alert'>
            <span>{message}</span>
        </div>)
}