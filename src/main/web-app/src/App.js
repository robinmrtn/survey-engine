import "./App.css";
import SurveySite from "./components/SurveySite";
import {BrowserRouter, Route, Switch} from "react-router-dom"

function App() {
    return (
        <div className="App">
            <BrowserRouter>
                <Switch>
                    <Route path='/survey/:surveyId'>
                        <SurveySite/>
                    </Route>
                    <Route path='/'>
                        <h1>Landing site</h1>
                    </Route>
                </Switch>
            </BrowserRouter>
        </div>
    );
}

export default App;
